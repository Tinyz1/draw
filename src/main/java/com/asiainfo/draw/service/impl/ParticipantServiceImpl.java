package com.asiainfo.draw.service.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
import com.asiainfo.draw.domain.LinkMember;
import com.asiainfo.draw.domain.LinkMemberExample;
import com.asiainfo.draw.domain.Participant;
import com.asiainfo.draw.domain.ParticipantExample;
import com.asiainfo.draw.exception.AuthenticationExceptioin;
import com.asiainfo.draw.persistence.LinkMemberMapper;
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
	private CurrentLinkCache currentLinkCache;

	@Autowired
	private LinkMemberMapper memberMapper;

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
	 * ֻ��ѯ�н�������ڵ���1�ε��û�
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Participant> queryAllParticipant() {
		ParticipantExample participantExample = new ParticipantExample();
		participantExample.createCriteria().andStateGreaterThanOrEqualTo(1);
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

		ParticipantExample participantExample = new ParticipantExample();
		// �齱�������0
		participantExample.createCriteria().andStateGreaterThan(0);
		List<Participant> participants = participantMapper.selectByExample(participantExample);

		// ��������
		List<Participant> values = new ArrayList<Participant>();

		// �Ѿ�ѡ��Ĳ�����Ա�����ٴ�ѡ�񡣼�ʹ���г齱����
		DrawLink link = (DrawLink) currentLinkCache.get(CurrentLinkCache.CURRENT_LINK);

		LinkMemberExample memberExample = new LinkMemberExample();
		memberExample.createCriteria().andLinkIdEqualTo(link.getLinkId()).andStateEqualTo(1);
		List<LinkMember> members = memberMapper.selectByExample(memberExample);

		Set<Integer> memSet = new HashSet<Integer>();
		for (LinkMember member : members) {
			memSet.add(member.getParticipantId());
		}

		for (Participant participant : participants) {
			if (memSet.contains(participant.getParticipantId())) {
				logger.info("�û�:{}���ڱ����ڳ齱��Ա�У��������ٴ�ѡȡ��");
				continue;
			} else {
				logger.info("�û�:{}->�齱����Ϊ:{}", participant.getParticipantName(), participant.getState());
				for (int i = 0; i < participant.getState(); i++) {
					values.add(participant);
				}
			}
		}

		return values;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Participant> getCurrentPickParticipant() {
		return (List<Participant>) currentLinkCache.get(CurrentLinkCache.CURRENT_PARTICIPANTS);
	}

	@Override
	public void addPickParticipant(String ids) {
		checkNotNull(ids);
		// ����δ��ʼʱ���������
		LinkState linkState = (LinkState) currentLinkCache.get(CurrentLinkCache.CURRENT_STATE);
		if (!LinkState.INIT.equals(linkState)) {
			throw new RuntimeException("�����ѿ�ʼ�����������");
		}

		DrawLink currentLink = (DrawLink) currentLinkCache.get(CurrentLinkCache.CURRENT_LINK);

		String[] idss = ids.split(",");
		if (idss != null && idss.length > 0) {
			for (String id : idss) {
				if (StringUtils.isNotBlank(id)) {
					Integer participantId = Integer.parseInt(id);
					try {

						Participant participant = participantCache.get(participantId);
						if (participant == null) {
							throw new NullPointerException("�����û�ID" + participantId + "��ȡ�����û���Ϣ��");
						}

						// ��ǰ��Ա���
						LinkMember member = new LinkMember();
						member.setLinkId(currentLink.getLinkId());
						member.setParticipantId(participantId);
						member.setState(1);
						memberMapper.insert(member);

						// ��ǰ�û��ĳ齱�����1
						participant.setState(participant.getState() - 1);
						participantMapper.updateByPrimaryKeySelective(participant);

						// ���¼��ػ���
						participantCache.reload(participantId);

					} catch (Exception e) {
						logger.error("��ǰ��Ա�Ѳ���齱�����ܼ������룡");
					}
				}
			}

		}
	}

	@Override
	public void add(String participants) {
		checkNotNull(participants);
		String[] parts = participants.split(",");
		if (parts != null && parts.length > 0) {
			for (String part : parts) {
				if (StringUtils.isNotBlank(part)) {
					Participant participant = new Participant();
					participant.setParticipantName(part);
					participant.setState(1);
					participantMapper.insert(participant);
				}
			}
		}

		// ���¼��ػ���
		participantCache.reload();

	}

}
