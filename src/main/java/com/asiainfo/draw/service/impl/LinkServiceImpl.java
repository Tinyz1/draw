package com.asiainfo.draw.service.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asiainfo.draw.cache.CommandCache;
import com.asiainfo.draw.cache.CurrentLinkCache;
import com.asiainfo.draw.cache.CurrentLinkCache.LinkState;
import com.asiainfo.draw.cache.LinkHitPrizeCache;
import com.asiainfo.draw.cache.ParticipantCache;
import com.asiainfo.draw.domain.DrawLink;
import com.asiainfo.draw.domain.DrawLinkExample;
import com.asiainfo.draw.domain.DrawPrize;
import com.asiainfo.draw.domain.DrawPrizeExample;
import com.asiainfo.draw.domain.LinkItem;
import com.asiainfo.draw.domain.LinkMember;
import com.asiainfo.draw.domain.LinkMemberExample;
import com.asiainfo.draw.domain.Participant;
import com.asiainfo.draw.domain.PrizeItem;
import com.asiainfo.draw.domain.WinningRecord;
import com.asiainfo.draw.exception.StartLinkException;
import com.asiainfo.draw.persistence.DrawLinkMapper;
import com.asiainfo.draw.persistence.DrawPrizeMapper;
import com.asiainfo.draw.persistence.LinkMemberMapper;
import com.asiainfo.draw.persistence.ParticipantMapper;
import com.asiainfo.draw.service.LinkService;
import com.asiainfo.draw.service.RecordService;
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
	private LinkHitPrizeCache linkHitPrizeCache;

	@Autowired
	private CommandCache redirectCache;

	@Autowired
	private ParticipantMapper participantMapper;

	@Override
	public void initLink(Integer linkId) {

		try {
			logger.info("环节初始化->尝试结束当前环节...");
			finishCurrentLink();
		} catch (Exception e) {
			logger.warn(e.toString());
		}

		logger.info("<<===========读取新的环节...");
		// 获取下一环节
		DrawLink currentLink = linkMapper.selectByPrimaryKey(linkId);
		if (currentLink == null) {
			throw new RuntimeException("根据环节ID:" + linkId + "获取不到抽奖环节");
		}

		logger.info("环节初始化->当前环节:{}", currentLink.getLinkName());
		currentLinkCache.put(CurrentLinkCache.CURRENT_LINK, currentLink);

		logger.info("环节初始化->环节参与人员列表默认为空.");
		currentLinkCache.put(CurrentLinkCache.CURRENT_PARTICIPANTS, new ArrayList<Participant>());

		logger.info("环节初始化->剩余未抽奖人员为0.");
		currentLinkCache.put(CurrentLinkCache.CURRENT_REMAIN_NUM, 0);

		logger.info("环节初始化->环节中奖记录为空.");
		currentLinkCache.put(CurrentLinkCache.CURRENT_HIT, new HashMap<Integer, DrawPrize>());

		logger.info("环节初始化->环节摇奖记录为空.");
		currentLinkCache.put(CurrentLinkCache.CURRENT_SHAKE, new HashSet<Integer>());

		// 刚初始化时，当前环节不能抽奖
		logger.info("环节初始化->环节状态设置为：{}.", LinkState.INIT);
		currentLinkCache.put(CurrentLinkCache.CURRENT_STATE, LinkState.INIT);

		// ----------------------------------------------------------------------------
		// 修改数据库的环节状态为2(进行中)
		currentLink.setLinkState(2);
		linkMapper.updateByPrimaryKeySelective(currentLink);
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

	/**
	 * 结束当前环节
	 */
	private void finishCurrentLink() {
		DrawLink currentLink = null;
		try {
			currentLink = (DrawLink) currentLinkCache.get(CurrentLinkCache.CURRENT_LINK);
		} catch (Exception e) {
			logger.error("当前环节不存在！");
		}
		if (currentLink != null) {
			finishLink(currentLink.getLinkId());
		}
	}

	@Override
	public void finishLink(Integer linkId) {
		// 环节标志设置为3(已结束)
		DrawLink link = linkMapper.selectByPrimaryKey(linkId);
		link.setLinkState(3);
		linkMapper.updateByPrimaryKeySelective(link);

		logger.info(link.toString());

		logger.info("<<=========结束环节{}...", link.getLinkName());
		// 把当前环节的开关关闭
		currentLinkCache.put(CurrentLinkCache.CURRENT_STATE, LinkState.FINISH);

		// 把环节中奖记录写入库中
		try {
			@SuppressWarnings("unchecked")
			Map<Integer, DrawPrize> currentHits = (Map<Integer, DrawPrize>) currentLinkCache.get(CurrentLinkCache.CURRENT_HIT);
			if (currentHits != null) {
				for (Map.Entry<Integer, DrawPrize> hit : currentHits.entrySet()) {
					WinningRecord winningRecord = new WinningRecord();
					// 中奖环节
					winningRecord.setLinkId(link.getLinkId());
					// 用户ID
					winningRecord.setParticipantId(hit.getKey());
					// 奖品ID
					winningRecord.setPrizeId(hit.getValue().getPrizeId());
					recordService.saveRecord(winningRecord);
				}
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		// 清空当前缓存
		currentLinkCache.invalidateAll();
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
			logger.info("环节开始->把当前环节的状态设置为:{}", LinkState.RUN);
			// 把当前环节的开关打开
			currentLinkCache.put(CurrentLinkCache.CURRENT_STATE, LinkState.RUN);
		}
	}

	@Override
	public List<ParticipantPrize> getLinkHitPrize(Integer linkId) {
		checkNotNull(linkId);
		Map<String, DrawPrize> hitPrize = linkHitPrizeCache.get(linkId);
		if (hitPrize == null) {
			hitPrize = new HashMap<String, DrawPrize>();
		}

		List<ParticipantPrize> hitPrizes = new ArrayList<ParticipantPrize>();
		if (hitPrize != null) {
			for (Map.Entry<String, DrawPrize> hpriz : hitPrize.entrySet()) {
				ParticipantPrize ppr = new ParticipantPrize(getLinkByLinkId(linkId).getLinkName(), hpriz.getKey(), hpriz.getValue()
						.getPrizeType(), hpriz.getValue().getPrizeName());
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
	public List<DrawLink> getAll() {
		DrawLinkExample linkExample = new DrawLinkExample();
		return linkMapper.selectByExample(linkExample);
	}

	@Override
	public void resetLink(Integer linkId) {
		// 重置环节意味着，把已经结束的环节状态置为1（未开始）
		DrawLink link = linkMapper.selectByPrimaryKey(linkId);
		if (link == null) {
			throw new NullPointerException("环节为空");
		}
		link.setLinkState(1);
		linkMapper.updateByPrimaryKeySelective(link);
	}

	@Override
	public void add(LinkItem item) {
		// 新增抽奖环节
		DrawLink link = new DrawLink();
		link.setLinkName(item.getLinkName());
		// 只对未中奖的人开放
		link.setOpenState(1);
		// 未开始状态
		link.setLinkState(1);

		link.setLinkOrder(0);

		// 环节进入编码
		link.setEnterNumber(item.getEnterNumber());
		linkMapper.insert(link);

		// 目的：为了获取新增数据的ID
		DrawLinkExample linkExample = new DrawLinkExample();
		linkExample.createCriteria().andLinkNameEqualTo(item.getLinkName());
		link = linkMapper.selectByExample(linkExample).get(0);

		// 新增奖品
		List<PrizeItem> prizeItems = item.getPrizeItems();
		if (prizeItems != null && prizeItems.size() > 0) {
			for (PrizeItem prizeItem : prizeItems) {
				DrawPrize prize = new DrawPrize();
				prize.setLinkId(link.getLinkId());
				prize.setPrizeName(prizeItem.getPrizeName());
				prize.setPrizeType(prizeItem.getPrizeType());
				prize.setSize(prizeItem.getSize());
				prizeMapper.insert(prize);
			}
		}

	}

}
