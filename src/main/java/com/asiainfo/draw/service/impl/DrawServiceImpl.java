package com.asiainfo.draw.service.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asiainfo.draw.cache.CurrentLinkCache;
import com.asiainfo.draw.cache.CurrentLinkCache.LinkState;
import com.asiainfo.draw.cache.HitPrizeCache;
import com.asiainfo.draw.cache.LinkHitPrizeCache;
import com.asiainfo.draw.cache.ParticipantCache;
import com.asiainfo.draw.domain.DrawLink;
import com.asiainfo.draw.domain.DrawPrize;
import com.asiainfo.draw.domain.Participant;
import com.asiainfo.draw.service.DrawService;
import com.asiainfo.draw.service.LinkService;
import com.asiainfo.draw.util.Draw;
import com.asiainfo.draw.util.Prize;
import com.asiainfo.draw.util.PrizePool;

@Service("drawService")
@Transactional
public class DrawServiceImpl implements DrawService {

	private final Logger logger = LoggerFactory.getLogger(DrawServiceImpl.class);

	@Autowired
	private ParticipantCache participantCache;

	@Autowired
	private CurrentLinkCache currentLinkCache;

	@Autowired
	private HitPrizeCache hitPrizeCache;

	@Autowired
	private LinkService linkService;

	@Autowired
	private LinkHitPrizeCache linkHitPrizeCache;

	@SuppressWarnings("unchecked")
	@Override
	public Prize pick(String participantName, String enterNumber) {
		checkNotNull(participantName);

		DrawLink link = (DrawLink) currentLinkCache.get(CurrentLinkCache.CURRENT_LINK);
		LinkState linkState = (LinkState) currentLinkCache.get(CurrentLinkCache.CURRENT_STATE);
		logger.info("抽奖环节:{}的状态:{}", link.getLinkName(), linkState);

		switch (linkState) {
		// 环节未开始
		case INIT:
			logger.info("<<===========当前环节未开始！");
			return Prize.createInitPrize();
			// 环节运行中
		case RUN: {

			// 信息
			String mess;

			// 当前环节的进入编号
			String currentLinkEnterNumber = (String) currentLinkCache.get(CurrentLinkCache.CURRENT_ENTER_NUMBER);
			if (!StringUtils.equalsIgnoreCase(enterNumber, currentLinkEnterNumber)) {
				mess = "环节进入编号错误，不能参与抽奖";
				logger.warn(mess);
				throw new RuntimeException(mess);
			}

			// 获取当前用户
			Participant participant = participantCache.get(participantName);
			checkNotNull(participant, "根据用户: %s获取不到用户信息！", participantName);

			// 判断当前用户是否能够参与本环节抽奖
			List<Participant> allowParticipants = (List<Participant>) currentLinkCache.get(CurrentLinkCache.CURRENT_PARTICIPANTS);
			if (!allowParticipants.contains(participant)) {
				// 对于不在人员列表的用户，直接返回不中奖
				logger.info("用户：{}不在参与人员列表中，直接返回不中奖！", participantName);
				return Prize.createMissPrize();
			}

			// 一个人一个环节只能摇奖一次
			Set<Participant> currentShake = (Set<Participant>) currentLinkCache.get(CurrentLinkCache.CURRENT_SHAKE);
			if (currentShake.contains(participant)) {
				// 该人员当前环节已参与过抽奖，不能参与本次抽奖了
				logger.info("用户：{}当前环节已参与过抽奖，不能继续参与", participantName);
				return Prize.createMissPrize();
			} else {
				// 当前用户已经摇奖了
				currentShake.add(participant);
				currentLinkCache.put(CurrentLinkCache.CURRENT_SHAKE, currentShake);
			}

			// 满足抽奖条件的人员参与抽奖
			PrizePool pool = (PrizePool) currentLinkCache.get(CurrentLinkCache.CURRENT_POOL);

			// 随机抽奖
			DrawPrize drawPrize = Draw.pick(pool);

			if (drawPrize == null) {
				// 未中奖
				return Prize.createMissPrize();
			}

			// 已中奖
			Prize prize = new Prize(Prize.HIT, drawPrize.getPrizeType(), drawPrize.getPrizeName());
			logger.info("<<====参与人员：{},中奖：{}", participant, prize);

			// 记录环节中奖记录
			Map<Integer, DrawPrize> currentHits = (Map<Integer, DrawPrize>) currentLinkCache.get(CurrentLinkCache.CURRENT_HIT);
			// 更新当前环节中奖记录
			currentHits.put(participant.getParticipantId(), drawPrize);

			// 更新环节用户中奖记录
			Map<String, String> hitPrize = linkHitPrizeCache.get(link.getLinkId());
			if (hitPrize == null) {
				hitPrize = new HashMap<String, String>();
			}
			hitPrize.put(participantName, drawPrize.getPrizeName());
			linkHitPrizeCache.put(link.getLinkId(), hitPrize);

			if (!pool.hasPrize()) {
				logger.info("<<====当前环节剩余的奖品没有了时，结束当前环节");
				linkService.finishCurrentLink();
			}

			// 记录中奖记录
			hitPrizeCache.put(participant.getParticipantId(), drawPrize);
			return prize;
		}
		// 环节已结束
		case FINISH:
			logger.info("<<===========当前环节已结束！");
			return Prize.createOverPrize();
		}
		return Prize.createOverPrize();
	}
}
