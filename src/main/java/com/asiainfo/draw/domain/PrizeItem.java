package com.asiainfo.draw.domain;

import java.io.Serializable;

public class PrizeItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3989585763053700627L;

	private String prizeType;

	private String prizeName;

	private Integer size;

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

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((prizeName == null) ? 0 : prizeName.hashCode());
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
		PrizeItem other = (PrizeItem) obj;
		if (prizeName == null) {
			if (other.prizeName != null)
				return false;
		} else if (!prizeName.equals(other.prizeName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PrizeItem [prizeType=" + prizeType + ", prizeName=" + prizeName + ", size=" + size + "]";
	}

}