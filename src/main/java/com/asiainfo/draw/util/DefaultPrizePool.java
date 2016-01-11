package com.asiainfo.draw.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.draw.domain.DrawPrize;
import com.asiainfo.draw.exception.NoMorePrizeException;

public class DefaultPrizePool extends PrizePool {

	private final Logger logger = LoggerFactory.getLogger(DefaultPrizePool.class);

	private PrizePool prePool;

	public PrizePool getPrePool() {
		return prePool;
	}

	public void setPrePool(PrizePool prePool) {
		this.prePool = prePool;
	}

	public DefaultPrizePool() {
		super();
		init();
	}

	public DefaultPrizePool(List<DrawPrize> prizes) {
		super(prizes);
		init();
	}

	private void init() {
		setPrizes(new ArrayList<DrawPrize>());
		setName(getClass().getSimpleName() + "-" + new Random().nextInt());
	}

	@Override
	public int size() {
		return getPrizes().size();
	}

	@Override
	public boolean hasPrize() {
		return getPrizes().size() > 0;
	}

	@Override
	public synchronized DrawPrize pop(Boolean isHit) throws NoMorePrizeException {
		return pop(isHit, this);
	}

	public DrawPrize pop(Boolean isHit, DefaultPrizePool pool) {
		DrawPrize prize = null;
		if (hasPrize()) {
			int index = new Random().nextInt(size());
			prize = getPrizes().get(new Random().nextInt(size()));
			if (isHit != null) {
				// 要求必中奖
				if (isHit && prize == null) {
					logger.warn("<<==警告!!!!!!本次操作可能是作弊行为！！！需要必中！！！！开始执行必中逻辑！！！！！");
					if (getTruePrize() > 0) {
						while (prize == null) {
							index++;
							prize = getPrizes().get(index % getPrizes().size());
						}
					} else { // 当前奖品池没有真实奖品时，去前一个节点寻找，直到没有前一个节点为止
						if (isContinue(pool)) {
							logger.warn("<<==当前奖池没有真实奖品了==存在上级节点，去上一级节点寻找！");
							prize = ((DefaultPrizePool) getPrePool()).pop(isHit, pool);
						} else {
							logger.warn("<<==要求必中逻辑==不存在上级节点，放弃寻找，很遗憾，逻辑结束！!!!！");
						}
					}
				}
				// 要求不能中奖
				if (!isHit && prize != null) {
					logger.warn("<<==警告!!!!!!本次操作可能是作弊行为！！！需要不中！！！！开始执行不中逻辑！！！！！");
					// 当前只有一个真实的奖时，去父节点寻找
					if (getTruePrize() == getPrizes().size()) {
						if (isContinue(pool)) {
							logger.warn("<<==当前奖池全部是真实奖品==存在上级节点，去上一级节点寻找！");
							prize = ((DefaultPrizePool) getPrePool()).pop(isHit, pool);
						} else {
							logger.warn("<<==要求不中逻辑==不存在上级节点，放弃寻找，很遗憾，逻辑结束！!!!！");
						}
					} else {
						while (prize != null) {
							index++;
							prize = getPrizes().get(index % getPrizes().size());
						}
					}
				}
			}
			// 当前奖池剔除当前奖品
			List<DrawPrize> prizes = getPrizes();
			prizes.remove(index);
			setPrizes(prizes);
			// 已中奖时，需要把当前奖品池的真实奖品-1
			if (prize != null) {
				setTruePrize(getTruePrize() - 1);
			}
		} else {
			if (isContinue(pool)) {
				prize = ((DefaultPrizePool) getPrePool()).pop(isHit, pool);
			}
		}

		logger.debug("<<==" + getName() + " pop " + String.valueOf(prize));
		return prize;
	}

	/**
	 * 判断是否还需要调用上一个奖池的抽奖方法
	 * 
	 * @param pool
	 * @return
	 */
	private boolean isContinue(DefaultPrizePool pool) {
		return getPrePool() != null && getPrePool() != pool;
	}

	@Override
	public void push(DrawPrize prize) {
		logger.debug("<<==" + getName() + " push " + String.valueOf(prize));
		getPrizes().add(prize);
	}

}
