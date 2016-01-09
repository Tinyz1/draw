package com.asiainfo.draw.service.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
	public Participant getByParticipantId(Integer participantId) {
		checkNotNull(participantId);
		return participantMapper.selectByPrimaryKey(participantId);
	}

	@Override
	public void authParticipant(Integer participantNum, String participantName) {
		checkNotNull(participantNum);
		logger.debug("用户编号：" + participantNum);
		checkNotNull(participantName);
		logger.debug("用户名称：" + participantName);
		Participant participant;
		try {
			participant = participantCache.get(participantNum);
		} catch (Exception e) {
			logger.error("错误信息：" + e.toString());
			throw new AuthenticationExceptioin("用户编号不存在！");
		}
		if (participant == null) {
			throw new AuthenticationExceptioin("用户编号不存在！");
		}
		if (!StringUtils.equals(participantName, participant.getParticipantName())) {
			throw new AuthenticationExceptioin("用户姓名错误！");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<Participant> queryAllParticipant() {
		return participantMapper.selectByExample(new ParticipantExample());
	}

	@Override
	@Transactional(readOnly = true)
	public Participant getByParticipantNum(Integer participantNum) {
		checkNotNull(participantNum);
		return participantMapper.selectByParticipantNum(participantNum);
	}
}
