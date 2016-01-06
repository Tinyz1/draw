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
		// ��װ��ѯ�����������ֻ������ѯ
		ParticipantExample participantExample = new ParticipantExample();
		participantExample.createCriteria().andTelphoneEqualTo(participant.getTelphone());
		List<Participant> participants = participantMapper.selectByExample(participantExample);
		// һ������£��ֻ����벻���ظ�
		if (participants != null && participants.size() == 1) {
			Participant _participant = participants.get(0);
			// ����һ��Ψһ����Ÿ���ǰ�Ĳ����û�
			if(StringUtils.isBlank(_participant.getParticipantNumber())) {
				_participant.setParticipantNumber(ParticipantNumberGenerator.next());
			}
			// �����û��ĳƺ�
			if (StringUtils.isNotBlank(participant.getParticipantName())) {
				_participant.setParticipantName(participant.getParticipantName());
			}
			participantMapper.updateByPrimaryKey(_participant);
			return _participant;
		}
		// �����쳣�����ݣ����߲�ѯ���������ݣ�ֱ�ӷ���null����ʾ��֤ʧ�ܡ�
		return null;
	}

}
