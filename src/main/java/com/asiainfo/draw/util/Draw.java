package com.asiainfo.draw.util;

import java.io.Serializable;

import com.asiainfo.draw.domain.DrawPrize;
import com.asiainfo.draw.domain.Participant;
import com.asiainfo.draw.exception.NoMorePrizeException;
import com.google.common.base.Preconditions;

public final class Draw {

	public static Prize pick(Participant participant, PrizePool pool) {
		participant = Preconditions.checkNotNull(participant);
		pool = Preconditions.checkNotNull(pool);
		Prize _prize = new Prize(Prize.OVER, Prize.SPEC_OVER, "");
		if (pool.hasPrize()) {
			try {
				DrawPrize prize = pool.getOnePrize();
				// 此时表示中奖了
				if (prize != null) {
					// 记录中奖信息
					_prize.setType(Prize.HIT);
					_prize.setSepc("一等奖");
					_prize.setMess("1000");
				} else {
					_prize.setType(Prize.MISS);
					_prize.setSepc(Prize.SPEC_MISS);
					_prize.setMess("");
				}
			} catch (NoMorePrizeException e) {
				// 活动结束
			} finally {
				// 记录抽奖记录
			}
		}
		return _prize;
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
