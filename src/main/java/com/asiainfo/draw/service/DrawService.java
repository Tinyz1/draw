package com.asiainfo.draw.service;

import com.asiainfo.draw.util.Draw.Prize;

public interface DrawService {

	/**
	 * 根据房间号码和手机号码抽奖
	 * 
	 * @param roomId
	 *            房间号码
	 * @param phone
	 *            手机号码
	 * @return 抽奖结果
	 */
	Prize pick(Integer roomId, String phone);

}
