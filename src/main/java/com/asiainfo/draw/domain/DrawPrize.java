package com.asiainfo.draw.domain;

public class DrawPrize {
	private Integer prizeId;

	private String prizeType;

	private String prizeName;

	private Integer linkId;

	private Integer size;

	private String provider;

	private String providerPosition;

	public Integer getPrizeId() {
		return prizeId;
	}

	public void setPrizeId(Integer prizeId) {
		this.prizeId = prizeId;
	}

	public String getPrizeType() {
		return prizeType;
	}

	public void setPrizeType(String prizeType) {
		this.prizeType = prizeType == null ? null : prizeType.trim();
	}

	public String getPrizeName() {
		return prizeName;
	}

	public void setPrizeName(String prizeName) {
		this.prizeName = prizeName == null ? null : prizeName.trim();
	}

	public Integer getLinkId() {
		return linkId;
	}

	public void setLinkId(Integer linkId) {
		this.linkId = linkId;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider == null ? null : provider.trim();
	}

	public String getProviderPosition() {
		return providerPosition;
	}

	public void setProviderPosition(String providerPosition) {
		this.providerPosition = providerPosition == null ? null : providerPosition.trim();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((linkId == null) ? 0 : linkId.hashCode());
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
		DrawPrize other = (DrawPrize) obj;
		if (linkId == null) {
			if (other.linkId != null)
				return false;
		} else if (!linkId.equals(other.linkId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DrawPrize [prizeId=" + prizeId + ", prizeType=" + prizeType + ", prizeName=" + prizeName + ", linkId=" + linkId + ", size="
				+ size + ", provider=" + provider + ", providerPosition=" + providerPosition + "]";
	}

}