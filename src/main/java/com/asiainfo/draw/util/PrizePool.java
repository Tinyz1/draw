package com.asiainfo.draw.util;

import java.util.List;

import com.asiainfo.draw.domain.DrawPrize;
import com.asiainfo.draw.exception.NoMorePrizeException;

/**
 * 抽象奖品池
 * 
 * @author yecl
 *
 */
public abstract class PrizePool {

	/**
	 * 奖池名称
	 */
	private String name;

	/**
	 * 奖池里面的所有奖品（包括空的奖品）
	 */
	private List<DrawPrize> prizes;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<DrawPrize> getPrizes() {
		return prizes;
	}

	public void setPrizes(List<DrawPrize> prizes) {
		this.prizes = prizes;
	}

	public PrizePool() {
		super();
	}

	public PrizePool(List<DrawPrize> prizes) {
		super();
		this.prizes = prizes;
	}

	/**
	 * 返回奖品池的大小
	 * 
	 * @return
	 */
	public abstract int size();

	/**
	 * 判断奖池里面是否还有奖品
	 * 
	 * @return
	 */
	public abstract boolean hasPrize();

	/**
	 * 从奖池里面随机弹出一个奖品
	 * 
	 * @return 返回一个奖品，有可能为空
	 * @throws NoMorePrizeException
	 *             奖品池没有奖品时，会抛出此异常
	 */
	public abstract DrawPrize pop() throws NoMorePrizeException;

	/**
	 * 往奖品池里面增加奖品
	 * 
	 * @param prize
	 *            奖品
	 */
	public abstract void push(DrawPrize prize);
}
