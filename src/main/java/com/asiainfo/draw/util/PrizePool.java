package com.asiainfo.draw.util;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.asiainfo.draw.domain.DrawPrize;
import com.asiainfo.draw.exception.NoMorePrizeException;
import com.google.common.base.Preconditions;

/**
 * 奖品池
 * 
 * @author yecl
 *
 */
public final class PrizePool {

	/**
	 * 池大小
	 */
	private int size;

	/**
	 * 奖品池的所有奖品。包括空奖品
	 */
	private List<DrawPrize> allPrizes = new LinkedList<DrawPrize>();

	private static PrizePool pool;

	/**
	 * 初始化奖池时的默认乘积
	 */
	private int DEFAULT_TIMES = 10;

	private PrizePool() {
	}

	/**
	 * 
	 * @return
	 */
	public synchronized static PrizePool createPool() {
		if (pool != null) {
			return pool;
		}
		return new PrizePool();
	}

	/**
	 * 初始化奖池的奖品信息
	 */
	public PrizePool init(int numberOfPeople, List<DrawPrize> prizes) {
		Preconditions.checkArgument(numberOfPeople > 0, "参与人数不能小于0");
		Preconditions.checkArgument(prizes != null && prizes.size() > 0, "奖品池不能没有可中奖的奖品！");

		// 初始化之前，先清空奖池
		clear();

		size = numberOfPeople * prizes.size() * DEFAULT_TIMES;
		allPrizes.addAll(prizes);
		for (int i = 0, len = size - prizes.size(); i < len; i++) {
			allPrizes.add(null);
		}
		Collections.shuffle(allPrizes);
		return this;
	}

	/**
	 * 清空奖池
	 */
	public PrizePool clear() {
		size = 0;
		allPrizes.clear();
		return this;
	}

	public synchronized DrawPrize getOnePrize() {
		if (!hasPrize()) {
			throw new NoMorePrizeException("已经没有奖品可以抽取了。");
		}
		int pos = new Random().nextInt(size);
		DrawPrize prize = allPrizes.get(pos);
		allPrizes.remove(pos);
		size--;
		return prize;
	}

	public boolean hasPrize() {
		return size > 0;
	}
}