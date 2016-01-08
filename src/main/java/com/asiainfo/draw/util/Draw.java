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
				// ��ʱ��ʾ�н���
				if (prize != null) {
					// ��¼�н���Ϣ
					_prize.setType(Prize.HIT);
					_prize.setSepc("һ�Ƚ�");
					_prize.setMess("1000");
				} else {
					_prize.setType(Prize.MISS);
					_prize.setSepc(Prize.SPEC_MISS);
					_prize.setMess("");
				}
			} catch (NoMorePrizeException e) {
				// �����
			} finally {
				// ��¼�齱��¼
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
