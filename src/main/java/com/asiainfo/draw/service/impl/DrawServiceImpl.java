package com.asiainfo.draw.service.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

	@Override
	public Prize pick(String participantName) {
		checkNotNull(participantName);
		// 判断当前环节是否开始了
		LinkState linkState = (LinkState) currentLinkCache.get(CurrentLinkCache.CURRENT_STATE);
		switch (linkState) {
		// 环节未开始
		case INIT:
			logger.info("<<===========当前环节未开始！");
			return Prize.createInitPrize();
			// 环节运行中
		case RUN: {
			logger.info("<<=================用户:{}参与抽奖...", participantName);
			Participant participant = participantCache.get(participantName);
			checkNotNull(participant, "根据用户: %s获取不到用户信息！", participantName);

			// 判断当前环节是否对当前人员开发
			@SuppressWarnings("unchecked")
			List<Participant> allowParticipants = (List<Participant>) currentLinkCache.get(CurrentLinkCache.CURRENT_PARTICIPANTS);
			if (!allowParticipants.contains(participant)) {
				// 对于不在人员列表的用户，直接返回不中奖
				logger.info("用户：{}不在参与人员列表中，直接返回不中奖！");
				return Prize.createMissPrize();
			}

			// 一个人一个环节只能摇奖一次
			@SuppressWarnings("unchecked")
			Set<Integer> currentShake = (Set<Integer>) currentLinkCache.get(CurrentLinkCache.CURRENT_SHAKE);
			if (currentShake.contains(participant.getParticipantId())) {
				// 该人员当前环节已参与过抽奖，不能参与本次抽奖了
				logger.info("<<=================用户：{}当前环节已参与过抽奖，不能继续参与");
				return Prize.createMissPrize();
			} else {
				// 当前用户已经摇奖了
				currentShake.add(participant.getParticipantId());
				currentLinkCache.put(CurrentLinkCache.CURRENT_SHAKE, currentShake);
			}

			DrawLink link = (DrawLink) currentLinkCache.get(CurrentLinkCache.CURRENT_LINK);
			// 只对未中奖的人开放
			if (link.getLinkState() == DrawLink.LINK_CLOSE_TO_HIT_PRTICIPANT) {
				// 判断当前用户是否已中奖
				Set<DrawPrize> links = hitPrizeCache.get(participant.getParticipantId());
				if (links != null && links.size() > 0) {
					// 环节对中奖人员不开放，且已中奖的用户。直接返回没有中奖
					return Prize.createMissPrize();
				}
				// 当前人员未中奖时，可以继续抽奖
			}

			// 对于任何一种情况，任何一个人，同一个环节最多能够中奖一次。
			@SuppressWarnings("unchecked")
			Map<Integer, DrawPrize> currentHits = (Map<Integer, DrawPrize>) currentLinkCache.get(CurrentLinkCache.CURRENT_HIT);
			if (currentHits.containsKey(participant.getParticipantName())) {
				// 该人员当前环节已中奖，不能参与本次抽奖了
				return Prize.createMissPrize();
			}

			// 满足抽奖条件的人员参与抽奖
			PrizePool pool = (PrizePool) currentLinkCache.get(CurrentLinkCache.CURRENT_POOL);

			// 随机抽奖
			DrawPrize drawPrize = Draw.pick(pool, null);

			@SuppressWarnings("unchecked")
			// 当前环节剩余的奖品
			List<DrawPrize> currentPrizes = (List<DrawPrize>) currentLinkCache.get(CurrentLinkCache.CURRENT_PRIZES);
			// 当前环节没有中奖的人数 = 1 并且 只剩下一个奖品时。那么对于没有中奖的人，当前抽奖需要必中
			if (allowParticipants.size() - currentHits.size() == 1 && currentPrizes.size() == 1) {
				drawPrize = Draw.pick(pool, true); // 给这个人一次必中的机会
			}

			if (drawPrize == null) {
				// 未中奖
				return Prize.createMissPrize();
			}

			// 已中奖
			Prize prize = new Prize(Prize.HIT, drawPrize.getPrizeType(), drawPrize.getPrizeName());
			logger.info("<<====参与人员：{},中奖：{}", participant, prize);

			// 更新当前环节中奖记录
			currentHits.put(participant.getParticipantId(), drawPrize);
			currentLinkCache.put(CurrentLinkCache.CURRENT_HIT, currentHits);

			// 更新环节剩余奖品数
			currentPrizes.remove(drawPrize);
			currentLinkCache.put(CurrentLinkCache.CURRENT_PRIZES, currentPrizes);

			// 更新环节用户中奖记录
			Map<String, String> hitPrize = linkHitPrizeCache.get(link.getLinkId());
			if (hitPrize == null) {
				hitPrize = new HashMap<String, String>();
			}
			hitPrize.put(participantName, drawPrize.getPrizeName());
			linkHitPrizeCache.put(link.getLinkId(), hitPrize);

			if (currentPrizes.size() == 0) {
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
