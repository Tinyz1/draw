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
		// �жϵ�ǰ�����Ƿ�ʼ��
		LinkState linkState = (LinkState) currentLinkCache.get(CurrentLinkCache.CURRENT_STATE);
		switch (linkState) {
		// ����δ��ʼ
		case INIT:
			logger.info("<<===========��ǰ����δ��ʼ��");
			return Prize.createInitPrize();
			// ����������
		case RUN: {
			logger.info("<<=================�û�:{}����齱...", participantName);
			Participant participant = participantCache.get(participantName);
			checkNotNull(participant, "�����û�: %s��ȡ�����û���Ϣ��", participantName);

			// �жϵ�ǰ�����Ƿ�Ե�ǰ��Ա����
			@SuppressWarnings("unchecked")
			List<Participant> allowParticipants = (List<Participant>) currentLinkCache.get(CurrentLinkCache.CURRENT_PARTICIPANTS);
			if (!allowParticipants.contains(participant)) {
				// ���ڲ�����Ա�б���û���ֱ�ӷ��ز��н�
				logger.info("�û���{}���ڲ�����Ա�б��У�ֱ�ӷ��ز��н���");
				return Prize.createMissPrize();
			}

			// һ����һ������ֻ��ҡ��һ��
			@SuppressWarnings("unchecked")
			Set<Integer> currentShake = (Set<Integer>) currentLinkCache.get(CurrentLinkCache.CURRENT_SHAKE);
			if (currentShake.contains(participant.getParticipantId())) {
				// ����Ա��ǰ�����Ѳ�����齱�����ܲ��뱾�γ齱��
				logger.info("<<=================�û���{}��ǰ�����Ѳ�����齱�����ܼ�������");
				return Prize.createMissPrize();
			} else {
				// ��ǰ�û��Ѿ�ҡ����
				currentShake.add(participant.getParticipantId());
				currentLinkCache.put(CurrentLinkCache.CURRENT_SHAKE, currentShake);
			}

			DrawLink link = (DrawLink) currentLinkCache.get(CurrentLinkCache.CURRENT_LINK);
			// ֻ��δ�н����˿���
			if (link.getLinkState() == DrawLink.LINK_CLOSE_TO_HIT_PRTICIPANT) {
				// �жϵ�ǰ�û��Ƿ����н�
				Set<DrawPrize> links = hitPrizeCache.get(participant.getParticipantId());
				if (links != null && links.size() > 0) {
					// ���ڶ��н���Ա�����ţ������н����û���ֱ�ӷ���û���н�
					return Prize.createMissPrize();
				}
				// ��ǰ��Աδ�н�ʱ�����Լ����齱
			}

			// �����κ�һ��������κ�һ���ˣ�ͬһ����������ܹ��н�һ�Ρ�
			@SuppressWarnings("unchecked")
			Map<Integer, DrawPrize> currentHits = (Map<Integer, DrawPrize>) currentLinkCache.get(CurrentLinkCache.CURRENT_HIT);
			if (currentHits.containsKey(participant.getParticipantName())) {
				// ����Ա��ǰ�������н������ܲ��뱾�γ齱��
				return Prize.createMissPrize();
			}

			// ����齱��������Ա����齱
			PrizePool pool = (PrizePool) currentLinkCache.get(CurrentLinkCache.CURRENT_POOL);

			// ����齱
			DrawPrize drawPrize = Draw.pick(pool, null);

			@SuppressWarnings("unchecked")
			// ��ǰ����ʣ��Ľ�Ʒ
			List<DrawPrize> currentPrizes = (List<DrawPrize>) currentLinkCache.get(CurrentLinkCache.CURRENT_PRIZES);
			// ��ǰ����û���н������� = 1 ���� ֻʣ��һ����Ʒʱ����ô����û���н����ˣ���ǰ�齱��Ҫ����
			if (allowParticipants.size() - currentHits.size() == 1 && currentPrizes.size() == 1) {
				drawPrize = Draw.pick(pool, true); // �������һ�α��еĻ���
			}

			if (drawPrize == null) {
				// δ�н�
				return Prize.createMissPrize();
			}

			// ���н�
			Prize prize = new Prize(Prize.HIT, drawPrize.getPrizeType(), drawPrize.getPrizeName());
			logger.info("<<====������Ա��{},�н���{}", participant, prize);

			// ���µ�ǰ�����н���¼
			currentHits.put(participant.getParticipantId(), drawPrize);
			currentLinkCache.put(CurrentLinkCache.CURRENT_HIT, currentHits);

			// ���»���ʣ�ཱƷ��
			currentPrizes.remove(drawPrize);
			currentLinkCache.put(CurrentLinkCache.CURRENT_PRIZES, currentPrizes);

			// ���»����û��н���¼
			Map<String, String> hitPrize = linkHitPrizeCache.get(link.getLinkId());
			if (hitPrize == null) {
				hitPrize = new HashMap<String, String>();
			}
			hitPrize.put(participantName, drawPrize.getPrizeName());
			linkHitPrizeCache.put(link.getLinkId(), hitPrize);

			if (currentPrizes.size() == 0) {
				logger.info("<<====��ǰ����ʣ��Ľ�Ʒû����ʱ��������ǰ����");
				linkService.finishCurrentLink();
			}

			// ��¼�н���¼
			hitPrizeCache.put(participant.getParticipantId(), drawPrize);
			return prize;
		}
		// �����ѽ���
		case FINISH:
			logger.info("<<===========��ǰ�����ѽ�����");
			return Prize.createOverPrize();
		}
		return Prize.createOverPrize();
	}
}
