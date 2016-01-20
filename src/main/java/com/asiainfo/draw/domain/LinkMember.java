package com.asiainfo.draw.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 环节参与人员实体
 * 
 * @author yecl
 *
 */
public class LinkMember implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4525914894815374651L;

	/**
	 * ID
	 */
	private Integer memberId;

	/**
	 * 环节ID
	 */
	private Integer linkId;

	/**
	 * 环节名称
	 */
	private String linkName;

	/**
	 * 参与人员ID
	 */
	private Integer participantId;

	/**
	 * 参与人员名称
	 */
	private String participantName;

	/**
	 * 创建时间
	 */
	private Date createDate;

	/**
	 * 确认时间
	 */
	private Date confirmDate;

	/**
	 * 参与人员状态。1：未确认；2：已确认
	 */
	private Integer state;

	/**
	 * 1：未确认
	 */
	public static final int STATE_CREATE = 1;

	/**
	 * 2：已确认
	 */
	public static final int STATE_CONFIRM = 2;

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

	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName == null ? null : linkName.trim();
	}

	public Integer getParticipantId() {
		return participantId;
	}

	public void setParticipantId(Integer participantId) {
		this.participantId = participantId;
	}

	public String getParticipantName() {
		return participantName;
	}

	public void setParticipantName(String participantName) {
		this.participantName = participantName == null ? null : participantName.trim();
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getConfirmDate() {
		return confirmDate;
	}

	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((memberId == null) ? 0 : memberId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LinkMember other = (LinkMember) obj;
		if (memberId == null) {
			if (other.memberId != null)
				return false;
		} else if (!memberId.equals(other.memberId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LinkMember [memberId=" + memberId + ", linkName=" + linkName + ", participantName=" + participantName + ", state=" + state
				+ "]";
	}

}