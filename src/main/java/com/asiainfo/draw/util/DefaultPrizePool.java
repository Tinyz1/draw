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
	public synchronized DrawPrize pop() throws NoMorePrizeException {
		logger.info("奖池:{}拥有奖品数量:{}", size());
		DrawPrize prize = null;
		if (hasPrize()) {
			int index = new Random().nextInt(size());
			// 获取一个奖品
			prize = getPrizes().remove(index);
			logger.info("本次抽取的奖品:{}", prize);
		} else {
			throw new NoMorePrizeException("奖池里面没有更多的奖品了！");
		}
		return prize;
	}

	@Override
	public void push(DrawPrize prize) {
		logger.info("奖池:{}放入奖品:{}", getName(), prize);
		getPrizes().add(prize);
	}

}
