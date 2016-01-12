package com.asiainfo.draw.service.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asiainfo.draw.cache.AllPickCache;
import com.asiainfo.draw.cache.CurrentLinkCache;
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

	@Autowired
	private AllPickCache allPickCache;

	@Autowired
	private CurrentLinkCache currentLinkCache;

	@Override
	@Transactional(readOnly = true)
	public Participant getByParticipantName(String participantName) {
		checkNotNull(participantName);
		ParticipantExample participantExample = new ParticipantExample();
		participantExample.createCriteria().andParticipantNameEqualTo(participantName);
		List<Participant> participants = participantMapper.selectByExample(participantExample);

		Participant participant = null;
		if (participants != null && participants.size() > 0) {
			participant = participants.get(0);
		}
		logger.info("<<==========�����û�����:{}��ȡ���û�:{}.", participantName, participant);
		return participant;

	}

	/**
	 * ֻ��ѯ�Ѿ�������û�
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Participant> queryAllParticipant() {
		ParticipantExample participantExample = new ParticipantExample();
		participantExample.createCriteria().andStateEqualTo(2);
		return participantMapper.selectByExample(participantExample);
	}

	@Override
	public void authParticipant(String participantName) {
		// �����ж�ȡ�û�
		Participant participant = participantCache.get(participantName);
		if (participant == null) {
			String message = "�û���Ϊ:{}���û������ڣ�";
			logger.error(message, participantName);
			throw new AuthenticationExceptioin(message);
		}
		logger.info("�û�{}У��ͨ��", participantName);
	}

	@Override
	public Participant getByParticipantId(Integer participantId) {
		checkNotNull(participantId);
		return participantMapper.selectByPrimaryKey(participantId);
	}

	@Override
	public List<Participant> getCurrentlinkParticipant() {
		return allPickCache.getAll();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Participant> getCurrentPickParticipant() {
		return (List<Participant>) currentLinkCache.get(CurrentLinkCache.CURRENT_PARTICIPANTS);
	}

	@Override
	public void addPickParticipant(String ids) {
		checkNotNull(ids);
		@SuppressWarnings("unchecked")
		List<Participant> participants = (List<Participant>) currentLinkCache.get(CurrentLinkCache.CURRENT_PARTICIPANTS);
		String[] idss = ids.split(",");
		if (idss != null && idss.length > 0) {
			for (String id : idss) {
				if (StringUtils.isNotBlank(id)) {
					Integer iid = Integer.parseInt(id);
					try {
						Participant participant = allPickCache.get(iid);
						participants.add(participant);
						allPickCache.invalidate(iid);
					} catch (Exception e) {
						logger.error("��ǰ��Ա�Ѳ���齱�����ܼ������룡");
						throw new RuntimeException("��ǰ��Ա�Ѳ���齱�����ܼ�������!");
					}
				}
			}
			logger.info(participants.toString());
			currentLinkCache.put(CurrentLinkCache.CURRENT_PARTICIPANTS, participants);
		}
	}

}
