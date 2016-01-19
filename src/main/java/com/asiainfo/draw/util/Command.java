package com.asiainfo.draw.util;

import java.io.Serializable;

public class Command implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5272510467988436944L;

	/**
	 * ��ָ��
	 */
	public static final String COMMAND_NULL = "NULL";

	/**
	 * ��תָ��
	 */
	public static final String COMMAND_REDIRECT = "REDIRECT";

	/**
	 * ѡ�˿�ʼָ��
	 */
	public static final String ACTION_PICK_START = "PICK_START";

	/**
	 * ѡ�˽���ָ��
	 */
	public static final String ACTION_PICK_END = "PICK_END";

	/**
	 * ��ʼ������ָ��
	 */
	public static final String ACTION_INIT_POOL = "INIT_POOL";

	/**
	 * ���ƽ����Ƿ���Ҫ��ת
	 */
	private String type = COMMAND_NULL;

	/**
	 * ��תҳ���URL
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
	 * ����������תָ��
	 * 
	 * @param url
	 *            ��תURL
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
