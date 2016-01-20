package com.asiainfo.draw.service.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asiainfo.draw.cache.CurrentLinkCache;
import com.asiainfo.draw.cache.CurrentLinkCache.LinkState;
import com.asiainfo.draw.cache.ParticipantCache;
import com.asiainfo.draw.domain.DrawLink;
import com.asiainfo.draw.domain.DrawPrize;
import com.asiainfo.draw.domain.LinkMember;
import com.asiainfo.draw.domain.Participant;
import com.asiainfo.draw.domain.WinningRecord;
import com.asiainfo.draw.service.DrawService;
import com.asiainfo.draw.service.LinkMemberService;
import com.asiainfo.draw.service.RecordService;
import com.asiainfo.draw.util.Draw;
import com.asiainfo.draw.util.Prize;
import com.asiainfo.draw.util.PrizePool;

@Service("drawService")
@Transactional
public class DrawServiceImpl implements DrawService {

	@Autowired
	private ParticipantCache participantCache;

	@Autowired
	private CurrentLinkCache currentLinkCache;

	@Autowired
	private RecordService recordService;

	@Autowired
	private LinkMemberService memberService;

	@Override
	public Prize pick(String participantName, String enterNumber) {

		DrawLink link = (DrawLink) currentLinkCache.get(CurrentLinkCache.CURRENT_LINK);
		LinkState linkState = (LinkState) currentLinkCache.get(CurrentLinkCache.CURRENT_STATE);
		switch (linkState) {
		// ����δ��ʼ
		case INIT:
			return Prize.createInitPrize();
			// ����������
		case RUN: {

			// ��ȡ��ǰ�û�
			Participant participant = participantCache.get(participantName);
			checkNotNull(participant);

			// �жϵ�ǰ�û��Ƿ��ܹ����뱾���ڳ齱
			if (!memberService.isLinkContainMember(link.getLinkId(), participant.getParticipantId(), LinkMember.STATE_CONFIRM)) {
				return Prize.createMissPrize();
			}

			// ������֤����Ҫ��ȷ
			if (!StringUtils.equalsIgnoreCase(enterNumber, link.getEnterNumber())) {
				return Prize.createMissPrize("���Ļ�����֤�����");
			}

			// �û���ǰ����ҡ����¼
			List<WinningRecord> participantRecords = recordService.getRecordByParticipantNameAndLinkId(participantName, link.getLinkId());
			if (participantRecords != null && participantRecords.size() > 0) {
				// ��ǰ�����Ѿ��н���ֱ�ӷ��ص�ǰ���ڵ��н���Ϣ
				return new Prize(Prize.HIT, participantRecords.get(0).getPrizeType(), participantRecords.get(0).getPrizeName());
			}

			// ����齱
			DrawPrize drawPrize = Draw.pick((PrizePool) currentLinkCache.get(CurrentLinkCache.CURRENT_POOL));

			/* ��¼�н���¼ */
			WinningRecord record = new WinningRecord();
			record.setLinkId(link.getLinkId()); // ����ID
			record.setLinkName(link.getLinkName()); // ��������
			record.setParticipantId(participant.getParticipantId()); // �û�ID
			record.setParticipantName(participant.getParticipantName()); // �û�����
			record.setPrizeId(drawPrize.getPrizeId()); // ��ƷID
			record.setPrizeName(drawPrize.getPrizeName()); // ��Ʒ����
			record.setPrizeType(drawPrize.getPrizeType()); // ��Ʒ����
			recordService.saveRecord(record);

			// ���ؽ�Ʒ
			return new Prize(Prize.HIT, drawPrize.getPrizeType(), drawPrize.getPrizeName());

		}
		// �����ѽ���
		case FINISH:
			return Prize.createOverPrize();
		}

		return Prize.createOverPrize();
	}
}
