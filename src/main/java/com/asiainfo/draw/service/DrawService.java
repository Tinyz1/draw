package com.asiainfo.draw.service;

import com.asiainfo.draw.util.Prize;

public interface DrawService {

	/**
	 * �û��齱
	 * 
	 * @param participantName
	 *            �齱�û�
	 * @param enterNumber
	 *            �齱���
	 * @return ��Ʒ��Ϣ
	 */
	Prize pick(String participantName, String enterNumber);

}
