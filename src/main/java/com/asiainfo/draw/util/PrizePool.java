package com.asiainfo.draw.util;

import java.util.List;

import com.asiainfo.draw.domain.DrawPrize;
import com.asiainfo.draw.exception.NoMorePrizeException;

/**
 * ����Ʒ��
 * 
 * @author yecl
 *
 */
public abstract class PrizePool {

	/**
	 * ��������
	 */
	private String name;

	/**
	 * ������������н�Ʒ�������յĽ�Ʒ��
	 */
	private List<DrawPrize> prizes;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<DrawPrize> getPrizes() {
		return prizes;
	}

	public void setPrizes(List<DrawPrize> prizes) {
		this.prizes = prizes;
	}

	public PrizePool() {
		super();
	}

	public PrizePool(List<DrawPrize> prizes) {
		super();
		this.prizes = prizes;
	}

	/**
	 * ���ؽ�Ʒ�صĴ�С
	 * 
	 * @return
	 */
	public abstract int size();

	/**
	 * �жϽ��������Ƿ��н�Ʒ
	 * 
	 * @return
	 */
	public abstract boolean hasPrize();

	/**
	 * �ӽ��������������һ����Ʒ
	 * 
	 * @return ����һ����Ʒ���п���Ϊ��
	 * @throws NoMorePrizeException
	 *             ��Ʒ��û�н�Ʒʱ�����׳����쳣
	 */
	public abstract DrawPrize pop() throws NoMorePrizeException;

	/**
	 * ����Ʒ���������ӽ�Ʒ
	 * 
	 * @param prize
	 *            ��Ʒ
	 */
	public abstract void push(DrawPrize prize);
}
