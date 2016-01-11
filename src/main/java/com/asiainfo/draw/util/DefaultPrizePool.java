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
				// Ҫ����н�
				if (isHit && prize == null) {
					logger.warn("<<==����!!!!!!���β���������������Ϊ��������Ҫ���У���������ʼִ�б����߼�����������");
					if (getTruePrize() > 0) {
						while (prize == null) {
							index++;
							prize = getPrizes().get(index % getPrizes().size());
						}
					} else { // ��ǰ��Ʒ��û����ʵ��Ʒʱ��ȥǰһ���ڵ�Ѱ�ң�ֱ��û��ǰһ���ڵ�Ϊֹ
						if (isContinue(pool)) {
							logger.warn("<<==��ǰ����û����ʵ��Ʒ��==�����ϼ��ڵ㣬ȥ��һ���ڵ�Ѱ�ң�");
							prize = ((DefaultPrizePool) getPrePool()).pop(isHit, pool);
						} else {
							logger.warn("<<==Ҫ������߼�==�������ϼ��ڵ㣬����Ѱ�ң����ź����߼�������!!!��");
						}
					}
				}
				// Ҫ�����н�
				if (!isHit && prize != null) {
					logger.warn("<<==����!!!!!!���β���������������Ϊ��������Ҫ���У���������ʼִ�в����߼�����������");
					// ��ǰֻ��һ����ʵ�Ľ�ʱ��ȥ���ڵ�Ѱ��
					if (getTruePrize() == getPrizes().size()) {
						if (isContinue(pool)) {
							logger.warn("<<==��ǰ����ȫ������ʵ��Ʒ==�����ϼ��ڵ㣬ȥ��һ���ڵ�Ѱ�ң�");
							prize = ((DefaultPrizePool) getPrePool()).pop(isHit, pool);
						} else {
							logger.warn("<<==Ҫ�����߼�==�������ϼ��ڵ㣬����Ѱ�ң����ź����߼�������!!!��");
						}
					} else {
						while (prize != null) {
							index++;
							prize = getPrizes().get(index % getPrizes().size());
						}
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
		} else {
			if (isContinue(pool)) {
				prize = ((DefaultPrizePool) getPrePool()).pop(isHit, pool);
			}
		}

		logger.debug("<<==" + getName() + " pop " + String.valueOf(prize));
		return prize;
	}

	/**
	 * �ж��Ƿ���Ҫ������һ�����صĳ齱����
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
