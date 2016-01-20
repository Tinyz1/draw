package com.asiainfo.draw.service;

import java.util.List;

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
	void saveRecord(WinningRecord... records);

	/**
	 * 保存中奖记录
	 * 
	 * @param record
	 *            中奖记录
	 */
	void saveRecord(List<WinningRecord> records);

	/**
	 * 根据参与人员名称和环节ID获取中奖记录
	 * 
	 * @param partcipantName
	 *            参与人员名称
	 * @param linkId
	 *            环节ID
	 * @return 中奖记录
	 */
	List<WinningRecord> getRecordByParticipantNameAndLinkId(String partcipantName, Integer linkId);

}
