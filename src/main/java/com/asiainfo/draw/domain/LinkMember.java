package com.asiainfo.draw.domain;

public class LinkMember {
	private Integer memberId;

	private Integer linkId;

	private Integer participantId;

	private Integer state;

	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	public Integer getLinkId() {
		return linkId;
	}

	public void setLinkId(Integer linkId) {
		this.linkId = linkId;
	}

	public Integer getParticipantId() {
		return participantId;
	}

	public void setParticipantId(Integer participantId) {
		this.participantId = participantId;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "LinkMember [memberId=" + memberId + ", linkId=" + linkId + ", participantId=" + participantId + ", state=" + state + "]";
	}

}