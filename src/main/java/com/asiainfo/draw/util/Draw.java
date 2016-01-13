package com.asiainfo.draw.util;

import static com.google.common.base.Preconditions.checkNotNull;

import com.asiainfo.draw.domain.DrawPrize;

public final class Draw {

	/**
	 * 抽奖
	 * 
	 * @param prizePool
	 *            奖品池
	 * @param isHit
	 *            本次抽奖是否需要中奖。true:需要中奖，false:不能中奖；null：随机中奖。
	 * @return
	 */
	public static DrawPrize pick(PrizePool pool, Boolean isHit) {
		checkNotNull(pool);
		return pool.pop(isHit);
	}
}
