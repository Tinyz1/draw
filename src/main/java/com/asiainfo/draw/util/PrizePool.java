package com.asiainfo.draw.util;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.asiainfo.draw.domain.DrawPrize;
import com.asiainfo.draw.exception.NoMorePrizeException;
import com.google.common.base.Preconditions;

/**
 * ��Ʒ��
 * 
 * @author yecl
 *
 */
public final class PrizePool {

	/**
	 * �ش�С
	 */
	private int size;

	/**
	 * ��Ʒ�ص����н�Ʒ�������ս�Ʒ
	 */
	private List<DrawPrize> allPrizes = new LinkedList<DrawPrize>();

	private static PrizePool pool;

	/**
	 * ��ʼ������ʱ��Ĭ�ϳ˻�
	 */
	private int DEFAULT_TIMES = 10;

	private PrizePool() {
	}

	/**
	 * 
	 * @return
	 */
	public synchronized static PrizePool createPool() {
		if (pool != null) {
			return pool;
		}
		return new PrizePool();
	}

	/**
	 * ��ʼ�����صĽ�Ʒ��Ϣ
	 */
	public PrizePool init(int numberOfPeople, List<DrawPrize> prizes) {
		Preconditions.checkArgument(numberOfPeople > 0, "������������С��0");
		Preconditions.checkArgument(prizes != null && prizes.size() > 0, "��Ʒ�ز���û�п��н��Ľ�Ʒ��");

		// ��ʼ��֮ǰ������ս���
		clear();

		size = numberOfPeople * prizes.size() * DEFAULT_TIMES;
		allPrizes.addAll(prizes);
		for (int i = 0, len = size - prizes.size(); i < len; i++) {
			allPrizes.add(null);
		}
		Collections.shuffle(allPrizes);
		return this;
	}

	/**
	 * ��ս���
	 */
	public PrizePool clear() {
		size = 0;
		allPrizes.clear();
		return this;
	}

	public synchronized DrawPrize getOnePrize() {
		if (!hasPrize()) {
			throw new NoMorePrizeException("�Ѿ�û�н�Ʒ���Գ�ȡ�ˡ�");
		}
		int pos = new Random().nextInt(size);
		DrawPrize prize = allPrizes.get(pos);
		allPrizes.remove(pos);
		size--;
		return prize;
	}

	public boolean hasPrize() {
		return size > 0;
	}
}