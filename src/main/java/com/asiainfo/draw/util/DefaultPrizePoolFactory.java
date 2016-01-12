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
		checkArgument(numberOfPeople > 0, "参与人数不能小于0");
		logger.info("<<==参与人员：" + numberOfPeople);

		List<PrizePool> pools = new ArrayList<PrizePool>();
		// 一个奖池满足100个人
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
		logger.info("<<=====产生的奖池数量：" + numberOfPool);
		
		if (prizes != null && prizes.size() > 0) {
			// 放入真实的奖品
			for (int j = 0, len = prizes.size(); j < len; j++) {
				int poolNum = j % numberOfPool;
				PrizePool pool = pools.get(poolNum);
				pool.push(prizes.get(j));
				// 真实的奖品数量
				pool.setTruePrize(pool.getTruePrize() + prizes.get(j).getSize());
			}
		}

		int perPoolPrize = numberOfPeople / pools.size();

		logger.info("<<==每个奖池的总的奖品数量（包括空奖品）：" + perPoolPrize);

		for (PrizePool pool : pools) {
			for (int k = 0, len = perPoolPrize - pool.size(); k < len; k++) {
				pool.push(null);
			}
			logger.info("<<==奖池" + pool.getName() + "真实的奖品数量为：" + pool.getTruePrize());
			// 打乱奖品顺序
			Collections.shuffle(pool.getPrizes());
		}

		// 打乱奖池顺序
		Collections.shuffle(pools);
		return pools;
	}

}
