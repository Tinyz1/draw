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

		// 活动结束

		@SuppressWarnings("unchecked")
		List<PrizePool> pools = (List<PrizePool>) CurrentLinkCache.get("pools");
		int index = new Random().nextInt(pools.size());
		DrawPrize drawPrize = pools.get(index).pop(null);
		// 此时表示中奖了
		if (drawPrize != null) {
			// 记录中奖信息
			prize.setType(Prize.HIT);
			prize.setSepc(drawPrize.getPrizeType());
			prize.setMess(drawPrize.getPrizeName());
		} else {
			prize = Prize.createMissPrize();
		}
		// 记录抽奖记录
		return prize;
	}

	public static void main(String[] args) {
		List<DrawPrize> prizes = new ArrayList<DrawPrize>();
		DrawPrize prize1 = new DrawPrize();
		prize1.setPrizeName("1000元现金");
		prizes.add(prize1);
		DrawPrize prize2 = new DrawPrize();
		prize2.setPrizeName("10000元现金");
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
		 * 中奖
		 */
		public static final int HIT = 1;

		/**
		 * 未中奖
		 */
		public static final int MISS = 2;

		/**
		 * 未中奖规格
		 */
		public static final String SPEC_MISS = "再接再厉";

		/**
		 * 未中奖信息
		 */
		public static final String MESS_MISS = "使劲摇还是要有的，万一中奖了呢？";

		/**
		 * 抽奖结束
		 */
		public static final int OVER = 3;

		/**
		 * 活动结束规格
		 */
		public static final String SPEC_OVER = "活动结束";

		/**
		 * 活动结束信息
		 */
		public static final String MESS_OVER = "放松一下，马上回来！";

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
