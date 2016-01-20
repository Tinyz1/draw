package com.asiainfo.draw.service.impl;

import static com.google.common.base.Preconditions.checkArgument;
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
import com.asiainfo.draw.domain.Participant;
import com.asiainfo.draw.domain.ParticipantExample;
import com.asiainfo.draw.exception.AuthenticationExceptioin;
import com.asiainfo.draw.persistence.ParticipantMapper;
import com.asiainfo.draw.service.LinkMemberService;
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
	private LinkMemberService memberService;

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

	@Override
	public void auth(String participantName, String enterNum) {
		logger.info(participantName);
		checkNotNull(participantName);
		logger.info(enterNum);
		checkNotNull(enterNum);

		// 缓存中读取用户
		try {
			Participant participant = participantCache.get(participantName);
			if (participant == null) {
				throw new AuthenticationExceptioin("很遗憾，您不能参与抽奖！");
			}
		} catch (Exception e) {
			throw new AuthenticationExceptioin("很遗憾，您不能参与抽奖！");
		}

		DrawLink currentLink = null;
		try {
			currentLink = (DrawLink) currentLinkCache.get(CurrentLinkCache.CURRENT_LINK);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		if (currentLink == null) {
			throw new AuthenticationExceptioin("抽奖环节还没有开始，请稍等！");
		}
		if (!StringUtils.equalsIgnoreCase(enterNum, currentLink.getEnterNumber())) {
			throw new AuthenticationExceptioin("环节验证码错误，请确认是否输入有误！");
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

		DrawLink link = null;
		try {
			link = (DrawLink) currentLinkCache.get(CurrentLinkCache.CURRENT_LINK);
		} catch (Exception e) {
			logger.info(">>当前环节不存在！详细信息:{}", e);
		}

		// 返回数据
		List<Participant> values = new ArrayList<Participant>();
		if (link != null) {
			// 抽奖机会大于0
			ParticipantExample participantExample = new ParticipantExample();
			participantExample.createCriteria().andStateGreaterThan(0);
			List<Participant> participants = participantMapper.selectByExample(participantExample);

			List<LinkMember> members = memberService.getMemberByLinkIdAndState(link.getLinkId(), null);
			Set<Integer> memberIds = new HashSet<Integer>();
			if (members != null && members.size() > 0) {
				for (LinkMember member : members) {
					memberIds.add(member.getParticipantId());
				}
			}

			for (Participant participant : participants) {
				if (!memberIds.contains(participant.getParticipantId())) {
					logger.info("用户:{}->抽奖次数为:{}", participant.getParticipantName(), participant.getState());
					for (int i = 0, len = participant.getState(); i < len; i++) {
						values.add(participant);
					}
				}
			}

		}
		return values;
	}

	@Override
	public List<Participant> getCurrentPickParticipant() {
		DrawLink currentLink = null;
		try {
			currentLink = (DrawLink) currentLinkCache.get(CurrentLinkCache.CURRENT_LINK);
		} catch (Exception e) {
			logger.error("当前环节不存在！");
		}
		checkNotNull(currentLink);
		List<LinkMember> members = memberService.getMemberByLinkIdAndState(currentLink.getLinkId(), LinkMember.STATE_CONFIRM);

		List<Participant> participants = new ArrayList<Participant>();
		if (members != null && members.size() > 0) {
			for (LinkMember member : members) {
				Integer participantId = member.getParticipantId();
				participants.add(participantCache.get(participantId));
			}
		}
		return participants;
	}

	@Override
	public void addPickParticipant(String ids) {
		checkNotNull(ids);
		DrawLink link = null;
		try {
			link = (DrawLink) currentLinkCache.get(CurrentLinkCache.CURRENT_LINK);
		} catch (Exception e) {
			logger.info(">>当前环节不存在！详细信息:{}", e);
		}
		if (link != null) {
			// 环节未开始时才允许加人
			LinkState linkState = (LinkState) currentLinkCache.get(CurrentLinkCache.CURRENT_STATE);
			if (!LinkState.INIT.equals(linkState)) {
				throw new RuntimeException("环节已开始，不允许加人");
			}
			String[] idss = ids.split(",");
			if (idss != null && idss.length > 0) {
				List<LinkMember> members = new ArrayList<LinkMember>();
				for (String id : idss) {
					if (StringUtils.isNotBlank(id)) {
						Participant participant = participantCache.get(Integer.parseInt(id));
						if (participant != null) {
							// 加入环节参与人员表中。状态为未确认
							LinkMember member = new LinkMember();
							member.setLinkId(link.getLinkId());
							member.setLinkName(link.getLinkName());
							member.setParticipantId(participant.getParticipantId());
							member.setParticipantName(participant.getParticipantName());
							members.add(member);
						}
					}
				}
				memberService.create(members);
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

	@Override
	public void subShakeTime(List<Participant> participants) {
		checkArgument(participants != null && participants.size() > 0);
		for (Participant participant : participants) {
			checkNotNull(participant);
			participant.setState(participant.getState() - 1 >= 0 ? participant.getState() - 1 : participant.getState());
			participantMapper.updateByPrimaryKeySelective(participant);
		}
	}

	@Override
	public List<Participant> queryAllParticipant() {
		ParticipantExample participantExample = new ParticipantExample();
		participantExample.createCriteria().andStateGreaterThan(0);
		return participantMapper.selectByExample(participantExample);
	}

}
