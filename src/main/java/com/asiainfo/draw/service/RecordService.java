package com.asiainfo.draw.service;

import com.asiainfo.draw.domain.WinningRecord;

/**
 * 中奖记录服务接口
 * 
 * @author yecl
 *
 */
public interface RecordService {

	/**
	 * 保存中奖记录
	 * 
	 * @param record
	 *            中奖记录
	 */
	void saveRecord(WinningRecord record);

}
