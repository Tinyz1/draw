package com.asiainfo.draw.util;

import java.io.Serializable;

public class Command implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5272510467988436944L;

	/**
	 * 空指令
	 */
	public static final String COMMAND_NULL = "NULL";

	/**
	 * 跳转指令
	 */
	public static final String COMMAND_REDIRECT = "REDIRECT";

	/**
	 * 选人开始指令
	 */
	public static final String ACTION_PICK_START = "PICK_START";

	/**
	 * 选人结束指令
	 */
	public static final String ACTION_PICK_END = "PICK_END";

	/**
	 * 初始化奖池指令
	 */
	public static final String ACTION_INIT_POOL = "INIT_POOL";

	/**
	 * 控制界面是否需要跳转
	 */
	private String type = COMMAND_NULL;

	/**
	 * 跳转页面的URL
	 */
	private String url;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 创建界面跳转指令
	 * 
	 * @param url
	 *            跳转URL
	 * @return
	 */
	public static Command redirect(String url) {
		Command command = new Command();
		command.setType(Command.COMMAND_REDIRECT);
		command.setUrl(url);
		return command;
	}

	@Override
	public String toString() {
		return "Command [type=" + type + ", url=" + url + "]";
	}

}
