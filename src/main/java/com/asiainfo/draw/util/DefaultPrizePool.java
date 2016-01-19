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
		logger.info("����:{}ӵ�н�Ʒ����:{}", size());
		DrawPrize prize = null;
		if (hasPrize()) {
			int index = new Random().nextInt(size());
			// ��ȡһ����Ʒ
			prize = getPrizes().remove(index);
			logger.info("���γ�ȡ�Ľ�Ʒ:{}", prize);
		} else {
			throw new NoMorePrizeException("��������û�и���Ľ�Ʒ�ˣ�");
		}
		return prize;
	}

	@Override
	public void push(DrawPrize prize) {
		logger.info("����:{}���뽱Ʒ:{}", getName(), prize);
		getPrizes().add(prize);
	}

}
