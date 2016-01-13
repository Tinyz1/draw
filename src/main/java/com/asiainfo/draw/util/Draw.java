package com.asiainfo.draw.util;

import static com.google.common.base.Preconditions.checkNotNull;

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
	public static DrawPrize pick(PrizePool pool, Boolean isHit) {
		checkNotNull(pool);
		return pool.pop(isHit);
	}
}
