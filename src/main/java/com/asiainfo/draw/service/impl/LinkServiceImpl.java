package com.asiainfo.draw.service.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asiainfo.draw.cache.CurrentLinkCache;
import com.asiainfo.draw.cache.CurrentLinkCache.LinkState;
import com.asiainfo.draw.cache.ParticipantCache;
import com.asiainfo.draw.domain.DrawLink;
import com.asiainfo.draw.domain.DrawPrize;
import com.asiainfo.draw.domain.DrawPrizeExample;
import com.asiainfo.draw.domain.Participant;
import com.asiainfo.draw.domain.WinningRecord;
import com.asiainfo.draw.exception.StartLinkException;
import com.asiainfo.draw.persistence.DrawLinkMapper;
import com.asiainfo.draw.persistence.DrawPrizeMapper;
import com.asiainfo.draw.persistence.LinkMemberMapper;
import com.asiainfo.draw.persistence.WinningRecordMapper;
import com.asiainfo.draw.service.LinkService;
import com.asiainfo.draw.util.DefaultPrizePoolFactory;
import com.asiainfo.draw.util.PrizePool;
import com.asiainfo.draw.util.PrizePoolFactory;

@Service("linkService")
@Transactional
public class LinkServiceImpl implements LinkService {

	private final Logger logger = LoggerFactory.getLogger(LinkServiceImpl.class);

	@Autowired
	private DrawLinkMapper linkMapper;

	@Autowired
	private DrawPrizeMapper prizeMapper;

	@Autowired
	private LinkMemberMapper memberMapper;

	@Autowired
	private CurrentLinkCache currentLinkCache;

	@Autowired
	private ParticipantCache participantCache;
	
	@Autowired
	private WinningRecordMapper winningRecordMapper;
	
	@Override
	public void initNextLink() {
		logger.info("<<===========读取新的环节...");
		// 获取下一环节
		DrawLink currentLink = nextLink();
		if (currentLink == null) {
			logger.warn("<<==============没有下一个抽奖环节了！");
			return;
		}
		logger.info("<<===========已读取新的环节：{}...", currentLink.getLinkName());
		logger.info("<<===========把当前环节加入缓存中...");
		currentLinkCache.put(CurrentLinkCache.CURRENT_LINK, currentLink);

		logger.info("<<===========读取新的环节奖品...");
		List<DrawPrize> currentPrizes = getPrizeByLink(currentLink.getLinkId());

		logger.info("<<===========把当前环节剩余的奖品放入缓存中");
		currentLinkCache.put(CurrentLinkCache.CURRENT_PRIZES, currentPrizes);

		// 初始化当前环节参与人员
		List<Participant> participants = getParticipantByLink(currentLink.getLinkId());
		int numberOfPeople = 0;
		if (participants == null || participants.size() == 0) {
			participants = participantCache.getAll();
			numberOfPeople = participants.size();
		} else {
			numberOfPeople = participants.size();
		}
		logger.info("<<===========当前环节的参与人员：{}", participants);
		currentLinkCache.put(CurrentLinkCache.CURRENT_PARTICIPANTS, participants);

		logger.info("<<===========初始化当前环节已中奖人员...");
		currentLinkCache.put(CurrentLinkCache.CURRENT_HIT, new HashMap<String, DrawPrize>());

		logger.info("<<===========初始化奖品池...");
		PrizePoolFactory poolFactory = new DefaultPrizePoolFactory();

		logger.info("<<===========当今环节参与人数：{}...", numberOfPeople);
		List<PrizePool> pools = poolFactory.createPrizePools(numberOfPeople, currentPrizes);

		logger.info("<<===========把奖品池加入缓存中...");
		currentLinkCache.put(CurrentLinkCache.CURRENT_POOLS, pools);

		// 刚初始化时，当前环节不能抽奖
		logger.info("<<===========初始化环节状态为：{}...", LinkState.INIT);
		currentLinkCache.put(CurrentLinkCache.CURRENT_STATE, LinkState.INIT);
	}

	/**
	 * 获取当前环节能够参与的人员。
	 * 
	 * @param linkId
	 *            环节ID
	 * @return 参与人员列表
	 */
	private List<Participant> getParticipantByLink(Integer linkId) {
		checkNotNull(linkId);
		List<Participant> participants = memberMapper.selectParticipantByLink(linkId);
		return participants;
	}

	@Override
	public DrawLink nextLink() {
		// 环节顺序最小且未开始的为下一个开始环节
		DrawLink nextLink = linkMapper.selectNextLink();
		logger.info("<<==下一环节：" + nextLink);
		return nextLink;
	}

	/**
	 * 根据环节ID查询环节奖品
	 * 
	 * @param linkId
	 *            环节ID
	 * @return 当前环节的所有奖品
	 */
	private List<DrawPrize> getPrizeByLink(Integer linkId) {
		checkNotNull(linkId);
		DrawPrizeExample prizeExample = new DrawPrizeExample();
		// 环节ID相等
		prizeExample.createCriteria().andLinkIdEqualTo(linkId);
		List<DrawPrize> prizes = prizeMapper.selectByExample(prizeExample);
		logger.debug("<<==查询条件：linkId->{}，查询结果：{}", linkId, prizes);
		return prizes;
	}

	@Override
	public void finishCurrentLink() {

		DrawLink currentLink = (DrawLink) currentLinkCache.get(CurrentLinkCache.CURRENT_LINK);
		logger.info("<<=========结束环节{}...", currentLink.getLinkName());
		// 把当前环节的开关关闭
		currentLinkCache.put(CurrentLinkCache.CURRENT_STATE, LinkState.FINISH);
		// 记录环节
		currentLinkCache.put(CurrentLinkCache.CURRENT_FINISH_DATE, new Date());
		// 把环节中奖记录写入库中
		@SuppressWarnings("unchecked")
		Map<String, DrawPrize> currentHits = (Map<String, DrawPrize>) currentLinkCache.get(CurrentLinkCache.CURRENT_HIT);
		if(currentHits != null) {
			for(Map.Entry<String, DrawPrize> hit : currentHits.entrySet()) {
				WinningRecord winningRecord = new WinningRecord();
				winningRecord.setLinkId(currentLink.getLinkId());
				winningRecord.setParticipantName(hit.getKey());
				winningRecord.setPrizeId(hit.getValue().getPrizeId());
				winningRecordMapper.insert(winningRecord);
			}
		}
		// 把当前环节设置的状态设置为已结束
		currentLink.setLinkState(3);
		linkMapper.updateByPrimaryKey(currentLink);
		// 清空当前缓存
		currentLinkCache.clear();
		// 初始化下一个环节
		initNextLink();
	}

	@Override
	public void startCurrentLink() {
		logger.info("<<========尝试启动新的抽奖环节...");
		// 当前环节还没有结束时，不能启动新的环节
		LinkState linkState = (LinkState) currentLinkCache.get(CurrentLinkCache.CURRENT_STATE);
		logger.info("<<=============当前环节状态：{}" + linkState);
		if (linkState == LinkState.RUN) {
			String msg = "当前环节已经处于启动状态，启动失败...";
			logger.info(msg);
			throw new StartLinkException(msg);
		} else if (linkState == LinkState.FINISH) {
			String msg = "新的环节未初始化，启动失败...";
			logger.info(msg);
			throw new StartLinkException(msg);
		} else if (linkState == LinkState.INIT) {
			logger.info("<<==========把当前环节的状态设置为：{}" + LinkState.RUN);
			// 把当前环节的开关打开
			currentLinkCache.put(CurrentLinkCache.CURRENT_STATE, LinkState.RUN);
			Date start = new Date();
			logger.info("完成新的环节启动，启动时间：{}" + start);
			// 记录环节开始时间
			currentLinkCache.put(CurrentLinkCache.CURRENT_START_DATE, start);
		}
	}

}
