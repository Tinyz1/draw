package com.asiainfo.draw.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
		 * 抽奖结束
		 */
		public static final int OVER = 3;

		/**
		 * 活动结束规格
		 */
		public static final String SPEC_OVER = "活动结束";

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
	
	
	public static void main(String[] args) {
		PrizePool pool = PrizePool.createPool();
		List<DrawPrize> prizes = new ArrayList<DrawPrize>();
		prizes.add(new DrawPrize());
		pool.init(2, prizes);
		
		System.out.println(Draw.pick(new Participant(), pool));
	}

}
