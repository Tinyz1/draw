package com.asiainfo.draw.util;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.draw.domain.DrawPrize;

public class DefaultPrizePoolFactory extends PrizePoolFactory {

	private final Logger logger = LoggerFactory.getLogger(DefaultPrizePoolFactory.class);

	@Override
	public List<PrizePool> createPrizePools(int numberOfPeople, List<DrawPrize> prizes) {
		checkArgument(numberOfPeople > 0, "������������С��0");
		logger.info("<<==������Ա��" + numberOfPeople);

		List<PrizePool> pools = new ArrayList<PrizePool>();
		// һ����������100����
		int i = 0;
		DefaultPrizePool prePool = null;
		DefaultPrizePool currPool = null;
		do {
			currPool = new DefaultPrizePool();
			currPool.setPrePool(prePool);
			prePool = currPool;
			pools.add(currPool);
			i += 100;
		} while (i < numberOfPeople);

		DefaultPrizePool firstPoll = (DefaultPrizePool) pools.get(0);
		firstPoll.setPrePool(currPool);

		int numberOfPool = pools.size();
		logger.info("<<=====�����Ľ���������" + numberOfPool);
		
		if (prizes != null && prizes.size() > 0) {
			// ������ʵ�Ľ�Ʒ
			for (int j = 0, len = prizes.size(); j < len; j++) {
				int poolNum = j % numberOfPool;
				PrizePool pool = pools.get(poolNum);
				pool.push(prizes.get(j));
				// ��ʵ�Ľ�Ʒ����
				pool.setTruePrize(pool.getTruePrize() + prizes.get(j).getSize());
			}
		}

		int perPoolPrize = numberOfPeople / pools.size();

		logger.info("<<==ÿ�����ص��ܵĽ�Ʒ�����������ս�Ʒ����" + perPoolPrize);

		for (PrizePool pool : pools) {
			for (int k = 0, len = perPoolPrize - pool.size(); k < len; k++) {
				pool.push(null);
			}
			logger.info("<<==����" + pool.getName() + "��ʵ�Ľ�Ʒ����Ϊ��" + pool.getTruePrize());
			// ���ҽ�Ʒ˳��
			Collections.shuffle(pool.getPrizes());
		}

		// ���ҽ���˳��
		Collections.shuffle(pools);
		return pools;
	}

}
