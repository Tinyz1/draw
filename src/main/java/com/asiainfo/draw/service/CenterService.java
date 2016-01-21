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
	 * 开始选人
	 */
	void startPickNum();

	/**
	 * 结束选人
	 */
	void endPickNum();

	/**
	 * 提交选人
	 */
	void commitPicNum();

	/**
	 * 取指令
	 * 
	 * @return
	 */
	Command getCommand(String identity);

	/**
	 * 一键抽奖
	 */
	void manual();

}
