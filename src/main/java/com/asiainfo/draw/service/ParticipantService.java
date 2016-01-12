package com.asiainfo.draw.service;

import java.util.List;

import com.asiainfo.draw.domain.Participant;

public interface ParticipantService {

	/**
	 * �����û�ID��ȡ�û���Ϣ
	 * 
	 * @param ParticipantId
	 *            �û�ID
	 * @return ������Ա��Ϣ
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

	/**
	 * ��ȡ�ɽ��г��˵���Ա
	 * 
	 * @return
	 */
	public List<Participant> getCurrentlinkParticipant();

	/**
	 * ��ȡ��ǰ���ڿɽ��г齱����Ա
	 * 
	 * @return
	 */
	public List<Participant> getCurrentPickParticipant();

	/**
	 * ��ҡ�е���Ա���뵱ǰ��ҡ������Ա��
	 * 
	 * @param ids
	 *            ��Աid,��Աid,...
	 */
	public void addPickParticipant(String ids);

}
