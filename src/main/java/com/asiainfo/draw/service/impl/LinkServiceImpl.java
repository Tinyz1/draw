package com.asiainfo.draw.service.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asiainfo.draw.cache.AllPickCache;
import com.asiainfo.draw.cache.CommandCache;
import com.asiainfo.draw.cache.CurrentLinkCache;
import com.asiainfo.draw.cache.CurrentLinkCache.LinkState;
import com.asiainfo.draw.cache.LinkHitPrizeCache;
import com.asiainfo.draw.cache.ParticipantCache;
import com.asiainfo.draw.domain.DrawLink;
import com.asiainfo.draw.domain.DrawPrize;
import com.asiainfo.draw.domain.DrawPrizeExample;
import com.asiainfo.draw.domain.LinkMember;
import com.asiainfo.draw.domain.LinkMemberExample;
import com.asiainfo.draw.domain.Participant;
import com.asiainfo.draw.domain.WinningRecord;
import com.asiainfo.draw.exception.StartLinkException;
import com.asiainfo.draw.persistence.DrawLinkMapper;
import com.asiainfo.draw.persistence.DrawPrizeMapper;
import com.asiainfo.draw.persistence.LinkMemberMapper;
import com.asiainfo.draw.persistence.ParticipantMapper;
import com.asiainfo.draw.service.LinkService;
import com.asiainfo.draw.service.RecordService;
import com.asiainfo.draw.util.Command;
import com.asiainfo.draw.util.DefaultPrizePoolFactory;
import com.asiainfo.draw.util.ParticipantPrize;
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
	private RecordService recordService;

	@Autowired
	private AllPickCache allPickCache;

	@Autowired
	private LinkHitPrizeCache linkHitPrizeCache;

	@Autowired
	private CommandCache redirectCache;

	@Autowired
	private ParticipantMapper participantMapper;

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

		logger.info("<<===========初始化当前环节可参与人员列表为空...");
		currentLinkCache.put(CurrentLinkCache.CURRENT_PARTICIPANTS, new ArrayList<Participant>());

		logger.info("<<===========初始化当前环节剩余未抽奖人员为0");
		currentLinkCache.put(CurrentLinkCache.CURRENT_REMAIN_NUM, 0);

		logger.info("<<===========初始化当前环节已中奖人员...");
		currentLinkCache.put(CurrentLinkCache.CURRENT_HIT, new HashMap<Integer, DrawPrize>());

		logger.info("<<===========初始化当前环节摇奖记录...");
		currentLinkCache.put(CurrentLinkCache.CURRENT_SHAKE, new HashSet<Integer>());

		// 刚初始化时，当前环节不能抽奖
		logger.info("<<===========初始化环节状态为：{}...", LinkState.INIT);
		currentLinkCache.put(CurrentLinkCache.CURRENT_STATE, LinkState.INIT);
	}

	/**
	 * 初始化奖品池，一般在选人后才进行初始化
	 */
	@Override
	public void initPool() {

		DrawLink currentLink = (DrawLink) currentLinkCache.get(CurrentLinkCache.CURRENT_LINK);

		logger.info("<<===========读取新的环节奖品...");
		List<DrawPrize> currentPrizes = getPrizeByLink(currentLink.getLinkId());

		logger.info("<<===========把当前环节奖品放入缓存中");
		currentLinkCache.put(CurrentLinkCache.CURRENT_PRIZES, currentPrizes);

		// 初始化当前环节参与人员
		LinkMemberExample memberExample = new LinkMemberExample();
		memberExample.createCriteria().andLinkIdEqualTo(currentLink.getLinkId()).andStateEqualTo(1);
		List<LinkMember> linkMembers = memberMapper.selectByExample(memberExample);
		if (linkMembers == null || linkMembers.size() == 0) {
			String mess = "抽奖参与人员不能为空";
			logger.info(mess);
			throw new RuntimeException(mess);
		}

		List<Participant> participants = new ArrayList<Participant>();
		for (LinkMember member : linkMembers) {
			Participant participant = participantCache.get(member.getParticipantId());
			participants.add(participant);

			// 中奖机会减少一次
			allPickCache.subTimes(participant.getParticipantId());

			// 更新库,机会减少一次
			participant.setState(allPickCache.get(participant.getParticipantId()));
			participantMapper.updateByPrimaryKey(participant);
		}
		currentLinkCache.put(CurrentLinkCache.CURRENT_PARTICIPANTS, participants);

		// 把已加入参与的人员状态设置为已使用
		for (LinkMember member : linkMembers) {
			member.setState(2);
			memberMapper.updateByPrimaryKey(member);
		}

		logger.info("<<===========初始化奖品池...");
		PrizePoolFactory poolFactory = new DefaultPrizePoolFactory();

		int numberOfPeople = participants.size();
		logger.info("<<===========当今环节参与人数：{}...", numberOfPeople);
		PrizePool pool = poolFactory.createPrizePools(numberOfPeople, currentPrizes);

		logger.info("<<===========把奖品池加入缓存中...");
		currentLinkCache.put(CurrentLinkCache.CURRENT_POOL, pool);

		// 界面跳转指令
		Command command = new Command();
		command.setType(Command.COMMAND_REDIRECT);
		command.setUrl("LuckyList.jsp");
		redirectCache.put(CommandCache.CURRENT_COMMAND, command);
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
		Map<Integer, DrawPrize> currentHits = (Map<Integer, DrawPrize>) currentLinkCache.get(CurrentLinkCache.CURRENT_HIT);
		if (currentHits != null) {
			for (Map.Entry<Integer, DrawPrize> hit : currentHits.entrySet()) {
				WinningRecord winningRecord = new WinningRecord();
				// 中奖环节
				winningRecord.setLinkId(currentLink.getLinkId());
				// 用户ID
				winningRecord.setParticipantId(hit.getKey());
				// 奖品ID
				winningRecord.setPrizeId(hit.getValue().getPrizeId());
				recordService.saveRecord(winningRecord);
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
		logger.info("<<=============当前环节状态:{}", linkState);
		if (linkState == LinkState.RUN) {
			String msg = "当前环节已经处于启动状态，启动失败...";
			logger.info(msg);
			throw new StartLinkException(msg);
		} else if (linkState == LinkState.FINISH) {
			String msg = "新的环节未初始化，启动失败...";
			logger.info(msg);
			throw new StartLinkException(msg);
		} else if (linkState == LinkState.INIT) {
			logger.info("<<==========把当前环节的状态设置为:{}", LinkState.RUN);
			// 把当前环节的开关打开
			currentLinkCache.put(CurrentLinkCache.CURRENT_STATE, LinkState.RUN);
			Date start = new Date();
			logger.info("完成新的环节启动，启动时间:{}", start);
			// 记录环节开始时间
			currentLinkCache.put(CurrentLinkCache.CURRENT_START_DATE, start);

			// 界面跳转指令
			Command command = new Command();
			command.setType(Command.COMMAND_REDIRECT);
			command.setUrl("LuckBubble.jsp");
			redirectCache.put(CommandCache.CURRENT_COMMAND, command);
		}
	}

	@Override
	public List<ParticipantPrize> getLinkHitPrize(Integer linkId) {
		checkNotNull(linkId);
		Map<String, String> hitPrize = linkHitPrizeCache.get(linkId);
		if (hitPrize == null) {
			hitPrize = new HashMap<String, String>();
		}

		List<ParticipantPrize> hitPrizes = new ArrayList<ParticipantPrize>();
		if (hitPrize != null) {
			for (Map.Entry<String, String> hpriz : hitPrize.entrySet()) {
				ParticipantPrize ppr = new ParticipantPrize(getLinkByLinkId(linkId).getLinkName(), hpriz.getKey(), hpriz.getValue());
				hitPrizes.add(ppr);
			}
		}
		return hitPrizes;
	}

	private DrawLink getLinkByLinkId(Integer linkId) {
		DrawLink currentLink = (DrawLink) currentLinkCache.get(CurrentLinkCache.CURRENT_LINK);
		if (currentLink.getLinkId().intValue() == linkId.intValue()) {
			return currentLink;
		}
		return linkMapper.selectByPrimaryKey(linkId);
	}

	@Override
	public DrawLink getCurrentLink() {
		return (DrawLink) currentLinkCache.get(CurrentLinkCache.CURRENT_LINK);
	}

	@Override
	public void authLinkNumber(String enterNumber) {
		checkNotNull(enterNumber);
		DrawLink currentLink = (DrawLink) currentLinkCache.get(CurrentLinkCache.CURRENT_LINK);
		logger.info("当前环节进入编码：{}", currentLink.getEnterNumber());
		if (!StringUtils.equalsIgnoreCase(enterNumber, currentLink.getEnterNumber())) {
			String mess = "环节进入编号错误，不能参与抽奖";
			logger.warn(mess);
			throw new RuntimeException(mess);
		}

	}

}
