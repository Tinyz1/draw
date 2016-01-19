package com.asiainfo.draw.service.impl;

import static com.google.common.base.Preconditions.checkNotNull;

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
import com.asiainfo.draw.cache.ParticipantCache;
import com.asiainfo.draw.domain.DrawLink;
import com.asiainfo.draw.domain.DrawPrize;
import com.asiainfo.draw.domain.Participant;
import com.asiainfo.draw.exception.EnterNumberErrorException;
import com.asiainfo.draw.persistence.ParticipantMapper;
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
	private LinkService linkService;

	@Autowired
	private ParticipantMapper participantMapper;

	@SuppressWarnings("unchecked")
	@Override
	public Prize pick(String participantName, String enterNumber) {
		checkNotNull(participantName);
		logger.info("����齱�û�:{},���ڱ���:{}", participantName, enterNumber);
		DrawLink link = (DrawLink) currentLinkCache.get(CurrentLinkCache.CURRENT_LINK);
		LinkState linkState = (LinkState) currentLinkCache.get(CurrentLinkCache.CURRENT_STATE);
		logger.info("�齱����:{}��״̬:{}", link.getLinkName(), linkState);

		switch (linkState) {
		// ����δ��ʼ
		case INIT:
			logger.info("<<===========��ǰ����δ��ʼ��");
			return Prize.createInitPrize();
			// ����������
		case RUN: {

			// ��Ϣ
			String mess;

			// ��ǰ���ڵĽ�����
			if (!StringUtils.equalsIgnoreCase(enterNumber, link.getEnterNumber())) {
				mess = "���ڽ����Ŵ��󣬲��ܲ���齱";
				logger.warn(mess);
				throw new EnterNumberErrorException(mess);
			}

			// ��ȡ��ǰ�û�
			Participant participant = participantCache.get(participantName);
			logger.info("��ȡ�û���Ϣ->�����û����ƣ�{}��ȡ���û���Ϣ��{}", participantName, participant);
			checkNotNull(participant, "�����û�: %s��ȡ�����û���Ϣ��", participantName);

			// �жϵ�ǰ�û��Ƿ��ܹ����뱾���ڳ齱
			List<Participant> allowParticipants = (List<Participant>) currentLinkCache.get(CurrentLinkCache.CURRENT_PARTICIPANTS);
			if (!allowParticipants.contains(participant)) {
				// ���ڲ�����Ա�б���û���ֱ�ӷ��ز��н�
				logger.info("�û���{}���ڲ�����Ա�б��У�ֱ�ӷ��ز��н���", participantName);
				return Prize.createMissPrize();
			}

			// һ����һ������ֻ��ҡ��һ��
			Set<Participant> currentShake = (Set<Participant>) currentLinkCache.get(CurrentLinkCache.CURRENT_SHAKE);
			if (currentShake.contains(participant)) {
				// ����Ա��ǰ�����Ѳ�����齱�����ܲ��뱾�γ齱��
				logger.info("�û���{}��ǰ�����Ѳ�����齱�����ܼ�������", participantName);
				return Prize.createMissPrize();
			} else {
				// ��ǰ�û��Ѿ�ҡ����
				currentShake.add(participant);
			}

			// ����齱��������Ա����齱
			PrizePool pool = (PrizePool) currentLinkCache.get(CurrentLinkCache.CURRENT_POOL);

			// ����齱
			DrawPrize drawPrize = Draw.pick(pool);

			if (drawPrize == null) {
				// δ�н�
				return Prize.createMissPrize();
			}

			// ���н�
			Prize prize = new Prize(Prize.HIT, drawPrize.getPrizeType(), drawPrize.getPrizeName());
			logger.info("<<====������Ա��{},�н���{}", participant, prize);

			// ��¼�����н���¼
			Map<Integer, DrawPrize> currentHits = (Map<Integer, DrawPrize>) currentLinkCache.get(CurrentLinkCache.CURRENT_HIT);
			// ���µ�ǰ�����н���¼
			currentHits.put(participant.getParticipantId(), drawPrize);

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
