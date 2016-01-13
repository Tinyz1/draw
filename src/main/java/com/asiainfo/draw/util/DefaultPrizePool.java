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
		DrawPrize prize = null;
		if (hasPrize()) {
			int index = new Random().nextInt(size());
			prize = getPrizes().get(index);
			if (isHit != null) {
				// 要求必中奖
				if (isHit && prize == null) {
					logger.warn("<<==警告!!!!!!本次操作可能是作弊行为！！！需要必中！！！！开始执行必中逻辑！！！！！");
					if (getTruePrize() > 0) {
						while (prize == null) {
							index++;
							prize = getPrizes().get(index % getPrizes().size());
						}
					}
				}
				// 要求不能中奖
				if (!isHit && prize != null) {
					logger.warn("<<==警告!!!!!!本次操作可能是作弊行为！！！需要不中！！！！开始执行不中逻辑！！！！！");
					while (prize != null) {
						index++;
						prize = getPrizes().get(index % getPrizes().size());
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
		}
		logger.info("<<==" + getName() + " pop " + String.valueOf(prize));
		return prize;
	}

	@Override
	public void push(DrawPrize prize) {
		logger.debug("<<==" + getName() + " push " + String.valueOf(prize));
		getPrizes().add(prize);
	}

}
