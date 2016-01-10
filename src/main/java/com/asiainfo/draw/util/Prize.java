package com.asiainfo.draw.util;

import java.io.Serializable;

public final class Prize implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4306318044087762070L;

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
	 * ��ǰ�齱�����ѽ���
	 */
	public static final int OVER = 3;

	/**
	 * ��ǰ�齱����δ��ʼ
	 */
	public static final int INIT = 4;

	/**
	 * ��������
	 */
	public static final String SPEC_INIT = "�齱δ��ʼ";

	/**
	 * �������Ϣ
	 */
	public static final String MESS_INIT = "���Եȣ����Ͽ�ʼ��";

	/**
	 * ��������
	 */
	public static final String SPEC_OVER = "�齱�ѽ���";

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
