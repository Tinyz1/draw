package com.asiainfo.draw.service;

import java.util.List;

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
	void saveRecord(WinningRecord... records);

	/**
	 * �����н���¼
	 * 
	 * @param record
	 *            �н���¼
	 */
	void saveRecord(List<WinningRecord> records);

}
