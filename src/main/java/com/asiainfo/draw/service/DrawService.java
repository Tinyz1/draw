package com.asiainfo.draw.service;

import com.asiainfo.draw.util.Prize;

public interface DrawService {

	/**
	 * �����û��������г齱
	 * 
	 * @param participantName
	 *            ������Ա����
	 * @return �齱��Ϣ
	 */
	Prize pick(String participantName);

}
