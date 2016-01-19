package com.asiainfo.draw.util;

import java.util.List;

import com.asiainfo.draw.domain.DrawPrize;

/**
 * 奖品池抽象工厂
 * 
 * @author yecl
 *
 */
public abstract class PrizePoolFactory {

	/**
	 * 根据抽奖人数和奖品数生成一序列的奖池。
	 * 
	 * @param prizes
	 *            奖品数
	 * @return 奖池
	 */
	public abstract PrizePool createPrizePools(List<DrawPrize> prizes);

}
