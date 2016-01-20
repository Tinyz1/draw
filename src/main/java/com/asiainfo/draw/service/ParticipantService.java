package com.asiainfo.draw.service;

import java.util.List;
import java.util.Set;

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
	 * ��֤
	 * 
	 * @param participantName
	 * @param enterNum
	 */
	public void auth(String participantName, String enterNum);

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

	public void add(String participants);

	/**
	 * ѡ����û��齱�������1��
	 * 
	 * @param participants
	 */
	void subShakeTime(Set<Participant> participants);

	/**
	 * ��ѯ�齱�������1���û�
	 * 
	 * @return
	 */
	public List<Participant> queryAllParticipant();

}
