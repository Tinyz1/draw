package com.asiainfo.draw.util;

import java.io.Serializable;

public class ParticipantPrize implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8511787307612899081L;

	private String linkName;

	private String participantName;

	private String prizeType;

	private String prizeName;
	
	public ParticipantPrize() {
		super();
	}

	public ParticipantPrize(String linkName, String participantName, String prizeType, String prizeName) {
		super();
		this.linkName = linkName;
		this.participantName = participantName;
		this.prizeType = prizeType;
		this.prizeName = prizeName;
	}

	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	public String getParticipantName() {
		return participantName;
	}

	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}

	public String getPrizeType() {
		return prizeType;
	}

	public void setPrizeType(String prizeType) {
		this.prizeType = prizeType;
	}

	public String getPrizeName() {
		return prizeName;
	}

	public void setPrizeName(String prizeName) {
		this.prizeName = prizeName;
	}

	@Override
	public String toString() {
		return "ParticipantPrize [linkName=" + linkName + ", participantName=" + participantName + ", prizeType=" + prizeType
				+ ", prizeName=" + prizeName + "]";
	}

}
