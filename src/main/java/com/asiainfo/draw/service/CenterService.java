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
	 * ȡָ��
	 * 
	 * @return
	 */
	Command getRedirect();

}
