package com.asiainfo.draw.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asiainfo.draw.domain.Participant;
import com.asiainfo.draw.domain.ParticipantExample;
import com.asiainfo.draw.persistence.ParticipantMapper;
import com.asiainfo.draw.service.ParticipantService;
import com.asiainfo.draw.util.ParticipantNumberGenerator;
import com.google.common.base.Preconditions;

@Service("participantService")
@Transactional
public class ParticipantServiceImpl implements ParticipantService {

	@Autowired
	private ParticipantMapper participantMapper;

	@Override
	public Participant authParticipant(Participant participant) {
		participant = Preconditions.checkNotNull(participant);
		// 组装查询条件。根据手机号码查询
		ParticipantExample participantExample = new ParticipantExample();
		participantExample.createCriteria().andTelphoneEqualTo(participant.getTelphone());
		List<Participant> participants = participantMapper.selectByExample(participantExample);
		// 一般情况下，手机号码不会重复
		if (participants != null && participants.size() == 1) {
			Participant _participant = participants.get(0);
			// 生成一个唯一的序号给当前的参与用户
			if(StringUtils.isBlank(_participant.getParticipantNumber())) {
				_participant.setParticipantNumber(ParticipantNumberGenerator.next());
			}
			// 更新用户的称呼
			if (StringUtils.isNotBlank(participant.getParticipantName())) {
				_participant.setParticipantName(participant.getParticipantName());
			}
			participantMapper.updateByPrimaryKey(_participant);
			return _participant;
		}
		// 对于异常的数据，或者查询不到的数据，直接返回null，表示验证失败。
		return null;
	}

}
