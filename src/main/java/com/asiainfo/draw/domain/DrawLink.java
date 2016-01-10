package com.asiainfo.draw.domain;

public class DrawLink {
	private Integer linkId;

	private String linkName;

	private Integer openState;

	private Integer linkOrder;

	private Integer linkState;

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

	public Integer getOpenState() {
		return openState;
	}

	public void setOpenState(Integer openState) {
		this.openState = openState;
	}

	public Integer getLinkOrder() {
		return linkOrder;
	}

	public void setLinkOrder(Integer linkOrder) {
		this.linkOrder = linkOrder;
	}

	public Integer getLinkState() {
		return linkState;
	}

	public void setLinkState(Integer linkState) {
		this.linkState = linkState;
	}

	@Override
	public String toString() {
		return "DrawLink [linkId=" + linkId + ", linkName=" + linkName + ", openState=" + openState + ", linkOrder=" + linkOrder
				+ ", linkState=" + linkState + "]";
	}

	/**
	 * 环节对已中奖的人不开放
	 */
	public static final int LINK_CLOSE_TO_HIT_PRTICIPANT = 1;

	/**
	 * 环节对已中奖的人开放
	 */
	public static final int LINK_OPEN_TO_HIT_PRTICIPANT = 2;

}