package com.asiainfo.draw.service;

import com.asiainfo.draw.util.Prize;

public interface DrawService {

	/**
	 * 用户抽奖
	 * 
	 * @param participantName
	 *            抽奖用户
	 * @param enterNumber
	 *            抽奖编号
	 * @return 奖品信息
	 */
	Prize pick(String participantName, String enterNumber);

}
