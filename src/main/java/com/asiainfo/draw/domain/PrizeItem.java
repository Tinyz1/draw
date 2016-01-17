package com.asiainfo.draw.domain;

import java.io.Serializable;

public class PrizeItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3989585763053700627L;

	private String prizeName;
	private Integer size;

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
	public String toString() {
		return "PrizeItem [prizeName=" + prizeName + ", size=" + size + "]";
	}

}