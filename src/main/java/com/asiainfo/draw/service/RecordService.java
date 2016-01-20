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

	/**
	 * ���ݲ�����Ա���ƺͻ���ID��ȡ�н���¼
	 * 
	 * @param partcipantName
	 *            ������Ա����
	 * @param linkId
	 *            ����ID
	 * @return �н���¼
	 */
	List<WinningRecord> getRecordByParticipantNameAndLinkId(String partcipantName, Integer linkId);

}
