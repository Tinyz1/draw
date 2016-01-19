package com.asiainfo.draw.util;

import java.util.List;

import com.asiainfo.draw.domain.DrawPrize;

/**
 * ��Ʒ�س��󹤳�
 * 
 * @author yecl
 *
 */
public abstract class PrizePoolFactory {

	/**
	 * ���ݳ齱�����ͽ�Ʒ������һ���еĽ��ء�
	 * 
	 * @param prizes
	 *            ��Ʒ��
	 * @return ����
	 */
	public abstract PrizePool createPrizePools(List<DrawPrize> prizes);

}
