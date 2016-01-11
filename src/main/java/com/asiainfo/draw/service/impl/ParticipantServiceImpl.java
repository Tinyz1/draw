package com.asiainfo.draw.service.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asiainfo.draw.cache.ParticipantCache;
import com.asiainfo.draw.domain.Participant;
import com.asiainfo.draw.domain.ParticipantExample;
import com.asiainfo.draw.exception.AuthenticationExceptioin;
import com.asiainfo.draw.persistence.ParticipantMapper;
import com.asiainfo.draw.service.ParticipantService;

@Service("participantService")
@Transactional
public class ParticipantServiceImpl implements ParticipantService {

	private final Logger logger = LoggerFactory.getLogger(ParticipantServiceImpl.class);

	@Autowired
	private ParticipantMapper participantMapper;

	@Autowired
	private ParticipantCache participantCache;

	@Override
	@Transactional(readOnly = true)
	public Participant getByParticipantName(String participantName) {
		checkNotNull(participantName);
		ParticipantExample participantExample = new ParticipantExample();
		participantExample.createCriteria().andParticipantNameEqualTo(participantName);
		List<Participant> participants = participantMapper.selectByExample(participantExample);
		if (participants != null && participants.size() > 0) {
			return participants.get(0);
		}
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Participant> queryAllParticipant() {
		return participantMapper.selectByExample(new ParticipantExample());
	}

	@Override
	public void authParticipant(String participantName) {
		logger("У���û���{}", participantName);
		// �����ж�ȡ�û�
		Participant participant = participantCache.get(participantName);
		if (participant == null) {
			logger("�û���{}�����ڣ�", participantName);
			throw new AuthenticationExceptioin("�û�������");
		}
		logger("�û���{}У��ͨ��", participantName);
	}

	private void logger(String mess, Object... args) {
		logger.info("<<================" + mess, args);
	}

}
