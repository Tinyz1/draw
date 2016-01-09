package com.asiainfo.draw.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.asiainfo.draw.cache.CurrentLinkCache;
import com.asiainfo.draw.domain.DrawPrize;
import com.asiainfo.draw.domain.Participant;
import com.google.common.base.Preconditions;

public final class Draw {

	public static Prize pick(Participant participant) {
		participant = Preconditions.checkNotNull(participant);
		Prize prize = Prize.createMissPrize();

		// �����

		@SuppressWarnings("unchecked")
		List<PrizePool> pools = (List<PrizePool>) CurrentLinkCache.get("pools");
		int index = new Random().nextInt(pools.size());
		DrawPrize drawPrize = pools.get(index).pop(null);
		// ��ʱ��ʾ�н���
		if (drawPrize != null) {
			// ��¼�н���Ϣ
			prize.setType(Prize.HIT);
			prize.setSepc(drawPrize.getPrizeType());
			prize.setMess(drawPrize.getPrizeName());
		} else {
			prize = Prize.createMissPrize();
		}
		// ��¼�齱��¼
		return prize;
	}

	public static void main(String[] args) {
		List<DrawPrize> prizes = new ArrayList<DrawPrize>();
		DrawPrize prize1 = new DrawPrize();
		prize1.setPrizeName("1000Ԫ�ֽ�");
		prizes.add(prize1);
		DrawPrize prize2 = new DrawPrize();
		prize2.setPrizeName("10000Ԫ�ֽ�");
		prizes.add(prize2);
		PrizePoolFactory poolFactory = new DefaultPrizePoolFactory();
		List<PrizePool> pools = poolFactory.createPrizePools(101, prizes);
		CurrentLinkCache.put("pools", pools);

		Participant participant = new Participant();
		Prize prize = Draw.pick(participant);
		int i = 1;
		while (prize.getType() != 1) {
			prize = Draw.pick(participant);
			System.out.println(++i);
		}
		System.out.println(prize);
	}

	public static class Prize implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -1091067185183995815L;

		/**
		 * �н�
		 */
		public static final int HIT = 1;

		/**
		 * δ�н�
		 */
		public static final int MISS = 2;

		/**
		 * δ�н����
		 */
		public static final String SPEC_MISS = "�ٽ�����";

		/**
		 * δ�н���Ϣ
		 */
		public static final String MESS_MISS = "ʹ��ҡ����Ҫ�еģ���һ�н����أ�";

		/**
		 * �齱����
		 */
		public static final int OVER = 3;

		/**
		 * ��������
		 */
		public static final String SPEC_OVER = "�����";

		/**
		 * �������Ϣ
		 */
		public static final String MESS_OVER = "����һ�£����ϻ�����";

		private int type;

		private String sepc;

		private String mess;

		public Prize() {
			super();
		}

		public Prize(int type, String sepc, String mess) {
			super();
			this.type = type;
			this.sepc = sepc;
			this.mess = mess;
		}

		public static Prize createMissPrize() {
			return createMissPrize(Prize.MESS_MISS);
		}

		public static Prize createMissPrize(String mess) {
			return new Prize(Prize.MISS, Prize.SPEC_MISS, mess);
		}

		public static Prize createOverPrize() {
			return createOverPrize(Prize.MESS_OVER);
		}

		public static Prize createOverPrize(String mess) {
			return new Prize(Prize.OVER, Prize.SPEC_OVER, mess);
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

		public String getSepc() {
			return sepc;
		}

		public void setSepc(String sepc) {
			this.sepc = sepc;
		}

		public String getMess() {
			return mess;
		}

		public void setMess(String mess) {
			this.mess = mess;
		}

		@Override
		public String toString() {
			return "Prize [type=" + type + ", sepc=" + sepc + ", mess=" + mess + "]";
		}

	}
}
