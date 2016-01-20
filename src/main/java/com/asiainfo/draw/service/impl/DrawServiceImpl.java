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
		// 环节未开始
		case INIT:
			return Prize.createInitPrize();
			// 环节运行中
		case RUN: {

			// 获取当前用户
			Participant participant = participantCache.get(participantName);
			checkNotNull(participant);

			// 判断当前用户是否能够参与本环节抽奖
			if (!memberService.isLinkContainMember(link.getLinkId(), participant.getParticipantId(), LinkMember.STATE_CONFIRM)) {
				return Prize.createMissPrize();
			}

			// 环节验证码需要正确
			if (!StringUtils.equalsIgnoreCase(enterNumber, link.getEnterNumber())) {
				return Prize.createMissPrize("您的环节验证码错误！");
			}

			// 用户当前环节摇奖记录
			List<WinningRecord> participantRecords = recordService.getRecordByParticipantNameAndLinkId(participantName, link.getLinkId());
			if (participantRecords != null && participantRecords.size() > 0) {
				// 当前环节已经中奖，直接返回当前环节的中奖信息
				return new Prize(Prize.HIT, participantRecords.get(0).getPrizeType(), participantRecords.get(0).getPrizeName());
			}

			// 随机抽奖
			DrawPrize drawPrize = Draw.pick((PrizePool) currentLinkCache.get(CurrentLinkCache.CURRENT_POOL));

			/* 记录中奖记录 */
			WinningRecord record = new WinningRecord();
			record.setLinkId(link.getLinkId()); // 环节ID
			record.setLinkName(link.getLinkName()); // 环节名称
			record.setParticipantId(participant.getParticipantId()); // 用户ID
			record.setParticipantName(participant.getParticipantName()); // 用户名称
			record.setPrizeId(drawPrize.getPrizeId()); // 奖品ID
			record.setPrizeName(drawPrize.getPrizeName()); // 奖品名称
			record.setPrizeType(drawPrize.getPrizeType()); // 奖品类型
			recordService.saveRecord(record);

			// 返回奖品
			return new Prize(Prize.HIT, drawPrize.getPrizeType(), drawPrize.getPrizeName());

		}
		// 环节已结束
		case FINISH:
			return Prize.createOverPrize();
		}

		return Prize.createOverPrize();
	}
}
