package com.asiainfo.draw.service.impl;

import static com.google.common.base.Preconditions.checkNotNull;

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

			DrawLink link = (DrawLink) currentLinkCache.get(CurrentLinkCache.CURRENT_LINK);
			// ֻ��δ�н����˿���
			if (link.getLinkState() == DrawLink.LINK_CLOSE_TO_HIT_PRTICIPANT) {
				// �жϵ�ǰ�û��Ƿ����н�
				Set<DrawPrize> links = hitPrizeCache.get(participantName);
				if (links != null && links.size() > 0) {
					// ���ڶ��н���Ա�����ţ������н����û���ֱ�ӷ���û���н�
					return Prize.createMissPrize();
				}
				// ��ǰ��Աδ�н�ʱ�����Լ����齱
			}

			// �����κ�һ��������κ�һ���ˣ�ͬһ����������ܹ��н�һ�Ρ�
			@SuppressWarnings("unchecked")
			Map<String, DrawPrize> currentHits = (Map<String, DrawPrize>) currentLinkCache.get(CurrentLinkCache.CURRENT_HIT);
			if (currentHits.containsKey(participant.getParticipantName())) {
				// ����Ա��ǰ�������н������ܲ��뱾�γ齱��
				return Prize.createMissPrize();
			}

			// ����齱��������Ա����齱
			@SuppressWarnings("unchecked")
			List<PrizePool> pools = (List<PrizePool>) currentLinkCache.get(CurrentLinkCache.CURRENT_POOLS);

			// ����齱
			DrawPrize drawPrize = Draw.pick(pools, null);

			@SuppressWarnings("unchecked")
			// ��ǰ����ʣ��Ľ�Ʒ
			List<DrawPrize> currentPrizes = (List<DrawPrize>) currentLinkCache.get(CurrentLinkCache.CURRENT_PRIZES);
			// ��ǰ����û���н������� = 1 ���� ֻʣ��һ����Ʒʱ����ô����û���н����ˣ���ǰ�齱��Ҫ����
			if (allowParticipants.size() - currentHits.size() == 1 && currentPrizes.size() == 1) {
				drawPrize = Draw.pick(pools, true); // �������һ�α��еĻ���
			}

			if (drawPrize == null) {
				// δ�н�
				return Prize.createMissPrize();
			}

			// ���н�
			Prize prize = new Prize(Prize.HIT, drawPrize.getPrizeType(), drawPrize.getPrizeName());
			logger.info("<<====������Ա��{},�н���{}", participant, prize);

			// ���µ�ǰ�����н���¼
			currentHits.put(participantName, drawPrize);
			currentLinkCache.put(CurrentLinkCache.CURRENT_HIT, currentHits);

			// ���»���ʣ�ཱƷ��
			currentPrizes.remove(drawPrize);
			currentLinkCache.put(CurrentLinkCache.CURRENT_PRIZES, currentPrizes);
			
			if (currentPrizes.size() == 0) {
				logger.info("<<====��ǰ����ʣ��Ľ�Ʒû����ʱ��������ǰ����");
				linkService.finishCurrentLink();
			}

			// ��¼�н���¼
			hitPrizeCache.put(participantName, drawPrize);
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
