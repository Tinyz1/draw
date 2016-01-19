package com.asiainfo.draw.util;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.draw.domain.DrawPrize;

public class DefaultPrizePoolFactory extends PrizePoolFactory {

	private final Logger logger = LoggerFactory.getLogger(DefaultPrizePoolFactory.class);

	@Override
	public PrizePool createPrizePools(List<DrawPrize> prizes) {
		logger.info("<<==========开始创建奖池...");
		PrizePool pool = new DefaultPrizePool();
		if (prizes != null && prizes.size() > 0) {

			logger.info("<<============开始放入真实的奖品...");
			for (int i = 0, len = prizes.size(); i < len; i++) {

				DrawPrize prize = prizes.get(i);

				int k = prize.getSize();
				logger.info("<<========奖品:{}含有奖品数量:{}", prize.getPrizeName(), k);

				if (k > 0) {
					for (int j = 0; j < k; j++) {
						prize.setSize(1);
						pool.push(prize);
					}
				}
			}
		}

		// 打乱奖品顺序
		Collections.shuffle(pool.getPrizes());
		logger.info("<<==========奖池创建完毕...");
		return pool;
	}

}
