package com.asiainfo.draw.service;

import com.asiainfo.draw.domain.Participant;

public interface ParticipantService {
	
	/**
	 * 认证成功时，返回认证通过的参与者；认证失败时，返回null
	 * 
	 * @param participant
	 * @return
	 */
	public Participant authParticipant(Participant participant);
	
	/**
	 * 根据参与人员ID查询人员
	 * 
	 * @param participantId 参与人员ID
	 * 
	 * @return 参与人员
	 */
	public Participant getByParticipantId(Integer participantId);

}
