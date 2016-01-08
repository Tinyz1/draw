package com.asiainfo.draw.service;

import com.asiainfo.draw.util.Draw.Prize;

public interface DrawService {

	/**
	 * 根据用户编号进行抽奖
	 * 
	 * @param participantNum
	 *            参与人员编号
	 * @return 抽奖信息
	 */
	Prize pick(Integer participantNum);

}
