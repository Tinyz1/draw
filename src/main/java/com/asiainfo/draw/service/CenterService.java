package com.asiainfo.draw.service;

import com.asiainfo.draw.util.Command;

public interface CenterService {

	/**
	 * ����ѡ�˻���
	 * 
	 * @param partnum
	 *            ѡȡ����Ա����
	 */
	void pickNum(Integer partnum);

	/**
	 * ��ʼѡ��
	 */
	void startPickNum();

	/**
	 * ����ѡ��
	 */
	void endPickNum();

	/**
	 * �ύѡ��
	 */
	void commitPicNum();

	/**
	 * ȡָ��
	 * 
	 * @return
	 */
	Command getCommand(String identity);

	/**
	 * һ���齱
	 */
	void manual();

}
