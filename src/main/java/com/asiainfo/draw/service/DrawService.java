package com.asiainfo.draw.service;

import com.asiainfo.draw.util.Draw.Prize;

public interface DrawService {

	/**
	 * ���ݷ��������ֻ�����齱
	 * 
	 * @param roomId
	 *            �������
	 * @param phone
	 *            �ֻ�����
	 * @return �齱���
	 */
	Prize pick(Integer roomId, String phone);

}
