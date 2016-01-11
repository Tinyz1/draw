package com.asiainfo.draw.service;

import com.asiainfo.draw.util.Prize;

public interface DrawService {

	/**
	 * 根据用户姓名进行抽奖
	 * 
	 * @param participantName
	 *            参与人员姓名
	 * @return 抽奖信息
	 */
	Prize pick(String participantName);

}
