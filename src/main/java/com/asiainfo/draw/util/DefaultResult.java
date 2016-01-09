package com.asiainfo.draw.util;

import java.io.Serializable;

/**
 * Ĭ�Ϸ��ؽ��
 * 
 * @author yecl
 *
 */
public class DefaultResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ���ر���
	 */
	private int returnCode;

	/**
	 * Ĭ�ϵ���ȷ����
	 */
	public static final int RETURN_CODE_SUCCESS = 1;

	/**
	 * Ĭ�ϵĴ��󷵻�
	 */
	public static final int RETURN_CODE_ERROR = 2;

	/**
	 * �������
	 */
	private int resultCode;

	/**
	 * �����Ϣ����
	 */
	private String resultMsg;

	public int getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(int returnCode) {
		this.returnCode = returnCode;
	}

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	/**
	 * ����ִ����ȷ�ķ��ؽ��
	 * 
	 * @param resultCode
	 *            �������
	 * @param resultMsg
	 *            ��Ϣ
	 * @return
	 */
	public static DefaultResult newSuccessInstance(int resultCode, String resultMsg) {
		return new DefaultResult(DefaultResult.RETURN_CODE_SUCCESS, resultCode, resultMsg);
	}

	/**
	 * ����ִ�д���ķ��ؽ��
	 * 
	 * @param resultCode
	 *            �������
	 * @param resultMsg
	 *            ��Ϣ
	 * @return
	 */
	public static DefaultResult newErrorInstance(int resultCode, String resultMsg) {
		return new DefaultResult(DefaultResult.RETURN_CODE_ERROR, resultCode, resultMsg);
	}

	public DefaultResult() {
		super();
	}

	public DefaultResult(int returnCode, int resultCode, String resultMsg) {
		super();
		this.returnCode = returnCode;
		this.resultCode = resultCode;
		this.resultMsg = resultMsg;
	}

	@Override
	public String toString() {
		return "DefaultResult [returnCode=" + returnCode + ", resultCode=" + resultCode + ", resultMsg=" + resultMsg + "]";
	}

}
