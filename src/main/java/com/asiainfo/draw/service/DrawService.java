package com.asiainfo.draw.service;

import com.asiainfo.draw.util.Draw.Prize;

public interface DrawService {

	/**
	 * �����û���Ž��г齱
	 * 
	 * @param participantNum
	 *            ������Ա���
	 * @return �齱��Ϣ
	 */
	Prize pick(Integer participantNum);

}
