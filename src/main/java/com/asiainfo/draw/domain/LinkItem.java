/**
 * 
 */
package com.asiainfo.draw.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @author yecl
 *
 */
public class LinkItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1114128945225956116L;

	/**
	 * 
	 */
	public LinkItem() {
	}

	private String linkName;

	private String enterNumber;

	private List<PrizeItem> prizeItems;

	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	public String getEnterNumber() {
		return enterNumber;
	}

	public void setEnterNumber(String enterNumber) {
		this.enterNumber = enterNumber;
	}

	public List<PrizeItem> getPrizeItems() {
		return prizeItems;
	}

	public void setPrizeItems(List<PrizeItem> prizeItems) {
		this.prizeItems = prizeItems;
	}

	@Override
	public String toString() {
		return "LinkItem [linkName=" + linkName + ", enterNumber=" + enterNumber + ", prizeItems=" + prizeItems + "]";
	}

}
