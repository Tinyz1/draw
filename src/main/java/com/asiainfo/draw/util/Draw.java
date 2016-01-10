package com.asiainfo.draw.util;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Random;

import com.asiainfo.draw.domain.DrawPrize;

public final class Draw {

	/**
	 * �齱
	 * 
	 * @param prizePool
	 *            ��Ʒ��
	 * @param isHit
	 *            ���γ齱�Ƿ���Ҫ�н���true:��Ҫ�н���false:�����н���null������н���
	 * @return
	 */
	public static DrawPrize pick(List<PrizePool> pools, Boolean isHit) {
		checkNotNull(pools);
		int index = new Random().nextInt(pools.size());
		PrizePool pool = pools.get(index);
		return pool.pop(isHit);
	}
}
