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
	 * ���ݲ�����Ա������ȡ������Ա
	 * 
	 * @param participantName
	 *            ������Ա����
	 * @return ������Ա
	 */
	public Participant getByParticipantName(String participantName);

	/**
	 * �����û���ź��û�����֤�û��Ƿ��ܹ�����
	 * 
	 * @param participantNum
	 *            �û����
	 * @param participantName
	 *            �û���
	 */
	public void authParticipant(String participantName);

	/**
	 * ��ѯ���еĲ�����Ա��Ϣ��
	 * 
	 * @return
	 */
	public List<Participant> queryAllParticipant();

}
