package com.asiainfo.draw.service;

import com.asiainfo.draw.domain.WinningRecord;

/**
 * �н���¼����ӿ�
 * 
 * @author yecl
 *
 */
public interface RecordService {

	/**
	 * �����н���¼
	 * 
	 * @param record
	 *            �н���¼
	 */
	void saveRecord(WinningRecord record);

}
