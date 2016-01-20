package com.asiainfo.draw.service.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asiainfo.draw.domain.LinkMember;
import com.asiainfo.draw.domain.LinkMemberExample;
import com.asiainfo.draw.domain.LinkMemberExample.Criteria;
import com.asiainfo.draw.persistence.LinkMemberMapper;
import com.asiainfo.draw.service.LinkMemberService;

@Service("linkMemeberService")
@Transactional
public class LinkMemberServiceImpl implements LinkMemberService {

	@Autowired
	private LinkMemberMapper memberMapper;

	@Override
	@Transactional(readOnly = true)
	public List<LinkMember> getMemberByLinkIdAndState(Integer linkId, Integer state) {
		checkNotNull(linkId);
		LinkMemberExample memberExample = new LinkMemberExample();
		memberExample.createCriteria().andLinkIdEqualTo(linkId);
		return memberMapper.selectByExample(memberExample);
	}

	@Override
	public void create(LinkMember... members) {
		checkArgument(members != null && members.length > 0);
		for (LinkMember member : members) {
			checkNotNull(member);
			member.setState(LinkMember.STATE_CREATE);
			member.setCreateDate(new Date());
			memberMapper.insert(member);
		}
	}

	@Override
	public void create(List<LinkMember> members) {
		checkArgument(members != null && members.size() > 0);
		create(members.toArray(new LinkMember[0]));
	}

	@Override
	public void confirm(LinkMember... members) {
		checkArgument(members != null && members.length > 0);
		for (LinkMember member : members) {
			checkNotNull(member);
			member.setState(LinkMember.STATE_CONFIRM);
			member.setConfirmDate(new Date());
			memberMapper.updateByPrimaryKeySelective(member);
		}
	}

	@Override
	public void confirm(List<LinkMember> members) {
		checkArgument(members != null && members.size() > 0);
		confirm(members.toArray(new LinkMember[0]));
	}

	@Override
	public boolean isLinkContainMember(Integer linkId, Integer participantId, Integer state) {

		checkNotNull(linkId);
		checkNotNull(participantId);

		LinkMemberExample memberExample = new LinkMemberExample();
		Criteria criteria = memberExample.createCriteria();
		criteria.andLinkIdEqualTo(linkId).andParticipantIdEqualTo(participantId);
		if (state != null) {
			criteria.andStateEqualTo(state);
		}

		List<LinkMember> members = memberMapper.selectByExample(memberExample);

		if (members != null && members.size() > 0) {
			return true;
		}

		return false;
	}
}
