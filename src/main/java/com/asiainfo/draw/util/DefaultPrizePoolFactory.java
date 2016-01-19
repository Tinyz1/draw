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
		logger.info("<<==========��ʼ��������...");
		PrizePool pool = new DefaultPrizePool();
		if (prizes != null && prizes.size() > 0) {

			logger.info("<<============��ʼ������ʵ�Ľ�Ʒ...");
			for (int i = 0, len = prizes.size(); i < len; i++) {

				DrawPrize prize = prizes.get(i);

				int k = prize.getSize();
				logger.info("<<========��Ʒ:{}���н�Ʒ����:{}", prize.getPrizeName(), k);

				if (k > 0) {
					for (int j = 0; j < k; j++) {
						prize.setSize(1);
						pool.push(prize);
					}
				}
			}
		}

		// ���ҽ�Ʒ˳��
		Collections.shuffle(pool.getPrizes());
		logger.info("<<==========���ش������...");
		return pool;
	}

}
