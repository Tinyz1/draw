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
		logger.info("<<==========根据用户名称:{}获取到用户:{}.", participantName, participant);
		return participant;

	}

	/**
	 * 只查询中奖机会大于等于1次的用户
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
		// 缓存中读取用户
		Participant participant = participantCache.get(participantName);
		if (participant == null) {
			String message = "用户名为:{}的用户不存在！";
			logger.error(message, participantName);
			throw new AuthenticationExceptioin(message);
		}
		logger.info("用户{}校验通过", participantName);
	}

	@Override
	public Participant getByParticipantId(Integer participantId) {
		checkNotNull(participantId);
		return participantMapper.selectByPrimaryKey(participantId);
	}

	@Override
	public List<Participant> getCurrentlinkParticipant() {

		ParticipantExample participantExample = new ParticipantExample();
		// 抽奖机会大于0
		participantExample.createCriteria().andStateGreaterThan(0);
		List<Participant> participants = participantMapper.selectByExample(participantExample);

		// 返回数据
		List<Participant> values = new ArrayList<Participant>();

		// 已经选择的参与人员不能再次选择。即使还有抽奖机会
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
				logger.info("用户:{}已在本环节抽奖人员中，不进行再次选取。");
				continue;
			} else {
				logger.info("用户:{}->抽奖次数为:{}", participant.getParticipantName(), participant.getState());
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
		// 环节未开始时才允许加人
		LinkState linkState = (LinkState) currentLinkCache.get(CurrentLinkCache.CURRENT_STATE);
		if (!LinkState.INIT.equals(linkState)) {
			throw new RuntimeException("环节已开始，不允许加人");
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
							throw new NullPointerException("根据用户ID" + participantId + "获取不到用户信息！");
						}

						// 当前人员入库
						LinkMember member = new LinkMember();
						member.setLinkId(currentLink.getLinkId());
						member.setParticipantId(participantId);
						member.setState(1);
						memberMapper.insert(member);

						// 当前用户的抽奖机会减1
						participant.setState(participant.getState() - 1);
						participantMapper.updateByPrimaryKeySelective(participant);

						// 重新加载缓存
						participantCache.reload(participantId);

					} catch (Exception e) {
						logger.error("当前人员已参与抽奖，不能继续参与！");
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

		// 重新加载缓存
		participantCache.reload();

	}

}
