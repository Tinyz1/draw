package com.asiainfo.draw.service;

import com.asiainfo.draw.domain.Participant;

public interface ParticipantService {
	
	/**
	 * ��֤�ɹ�ʱ��������֤ͨ���Ĳ����ߣ���֤ʧ��ʱ������null
	 * 
	 * @param participant
	 * @return
	 */
	public Participant authParticipant(Participant participant);
	
	/**
	 * ���ݲ�����ԱID��ѯ��Ա
	 * 
	 * @param participantId ������ԱID
	 * 
	 * @return ������Ա
	 */
	public Participant getByParticipantId(Integer participantId);

}
