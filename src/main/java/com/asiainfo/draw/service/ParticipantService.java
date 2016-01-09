package com.asiainfo.draw.service;

import java.util.List;

import com.asiainfo.draw.domain.Participant;

public interface ParticipantService {

	/**
	 * ���ݲ�����ԱID��ѯ��Ա
	 * 
	 * @param participantId
	 *            ������ԱID
	 * 
	 * @return ������Ա
	 */
	public Participant getByParticipantId(Integer participantId);

	/**
	 * ���ݲ�����Ա��Ų�ѯ��Ա
	 * 
	 * @param participantNum
	 *            ������Ա���
	 * 
	 * @return ������Ա
	 */
	public Participant getByParticipantNum(Integer participantNum);

	/**
	 * �����û���ź��û�����֤�û��Ƿ��ܹ�����
	 * 
	 * @param participantNum
	 *            �û����
	 * @param participantName
	 *            �û���
	 */
	public void authParticipant(Integer participantNum, String participantName);

	/**
	 * ��ѯ���еĲ�����Ա��Ϣ��
	 * 
	 * @return
	 */
	public List<Participant> queryAllParticipant();

}
