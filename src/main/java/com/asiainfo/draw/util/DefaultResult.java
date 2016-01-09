package com.asiainfo.draw.util;

import java.io.Serializable;

/**
 * 默认返回结果
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
	 * 返回编码
	 */
	private int returnCode;

	/**
	 * 默认的正确返回
	 */
	public static final int RETURN_CODE_SUCCESS = 1;

	/**
	 * 默认的错误返回
	 */
	public static final int RETURN_CODE_ERROR = 2;

	/**
	 * 结果编码
	 */
	private int resultCode;

	/**
	 * 结果消息描述
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
	 * 创建执行正确的返回结果
	 * 
	 * @param resultCode
	 *            结果编码
	 * @param resultMsg
	 *            消息
	 * @return
	 */
	public static DefaultResult newSuccessInstance(int resultCode, String resultMsg) {
		return new DefaultResult(DefaultResult.RETURN_CODE_SUCCESS, resultCode, resultMsg);
	}

	/**
	 * 创建执行错误的返回结果
	 * 
	 * @param resultCode
	 *            结果编码
	 * @param resultMsg
	 *            消息
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
