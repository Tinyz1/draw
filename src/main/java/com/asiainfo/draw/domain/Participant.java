package com.asiainfo.draw.domain;

public class Participant {
	private Integer participantId;

	private String participantName;

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

	@Override
	public String toString() {
		return "Participant [participantId=" + participantId + ", participantName=" + participantName + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((participantName == null) ? 0 : participantName.hashCode());
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
		Participant other = (Participant) obj;
		if (participantName == null) {
			if (other.participantName != null)
				return false;
		} else if (!participantName.equals(other.participantName))
			return false;
		return true;
	}

}