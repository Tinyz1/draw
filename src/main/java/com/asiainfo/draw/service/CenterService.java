package com.asiainfo.draw.service;

import com.asiainfo.draw.util.Command;

public interface CenterService {

	/**
	 * 启动选人环节
	 * 
	 * @param partnum
	 *            选取的人员数量
	 */
	void pickNum(Integer partnum);

	/**
	 * 取指令
	 * 
	 * @return
	 */
	Command getRedirect();

}
