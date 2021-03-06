package com.asiainfo.draw.util;

import java.io.Serializable;

public final class Prize implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4306318044087762070L;

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
	public static final String SPEC_MISS = "很遗憾";

	/**
	 * 未中奖信息
	 */
	public static final String MESS_MISS = "您不能参与当前抽奖环节！";

	/**
	 * 当前抽奖环节已结束
	 */
	public static final int OVER = 3;

	/**
	 * 当前抽奖环节未开始
	 */
	public static final int INIT = 4;

	/**
	 * 其他信息
	 */
	public static final int EXT = 5;

	/**
	 * 活动结束规格
	 */
	public static final String SPEC_INIT = "抽奖未开始";

	/**
	 * 活动结束信息
	 */
	public static final String MESS_INIT = "请稍等，马上开始！";

	/**
	 * 活动结束规格
	 */
	public static final String SPEC_OVER = "抽奖已结束";

	/**
	 * 活动结束信息
	 */
	public static final String MESS_OVER = "放松一下，马上回来！";

	private int type;

	private String spec;

	private String mess;

	public Prize() {
		super();
	}

	public Prize(int type, String sepc, String mess) {
		super();
		this.type = type;
		this.spec = sepc;
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

	public static Prize createInitPrize() {
		return createInitPrize(Prize.MESS_INIT);
	}

	public static Prize createInitPrize(String mess) {
		return new Prize(Prize.INIT, Prize.SPEC_INIT, mess);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getMess() {
		return mess;
	}

	public void setMess(String mess) {
		this.mess = mess;
	}

	@Override
	public String toString() {
		return "Prize [type=" + type + ", spec=" + spec + ", mess=" + mess + "]";
	}

}
