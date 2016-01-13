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
				// Ҫ����н�
				if (isHit && prize == null) {
					logger.warn("<<==����!!!!!!���β���������������Ϊ��������Ҫ���У���������ʼִ�б����߼�����������");
					if (getTruePrize() > 0) {
						while (prize == null) {
							index++;
							prize = getPrizes().get(index % getPrizes().size());
						}
					}
				}
				// Ҫ�����н�
				if (!isHit && prize != null) {
					logger.warn("<<==����!!!!!!���β���������������Ϊ��������Ҫ���У���������ʼִ�в����߼�����������");
					while (prize != null) {
						index++;
						prize = getPrizes().get(index % getPrizes().size());
					}
				}
			}
			// ��ǰ�����޳���ǰ��Ʒ
			List<DrawPrize> prizes = getPrizes();
			prizes.remove(index);
			setPrizes(prizes);
			// ���н�ʱ����Ҫ�ѵ�ǰ��Ʒ�ص���ʵ��Ʒ-1
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
