package com.asiainfo.draw.service;

import java.util.List;

import com.asiainfo.draw.domain.Participant;

public interface ParticipantService {

	/**
	 * 根据参与人员ID查询人员
	 * 
	 * @param participantId
	 *            参与人员ID
	 * 
	 * @return 参与人员
	 */
	public Participant getByParticipantId(Integer participantId);

	/**
	 * 根据参与人员编号查询人员
	 * 
	 * @param participantNum
	 *            参与人员编号
	 * 
	 * @return 参与人员
	 */
	public Participant getByParticipantNum(Integer participantNum);

	/**
	 * 根据用户编号和用户名验证用户是否能够参与活动
	 * 
	 * @param participantNum
	 *            用户编号
	 * @param participantName
	 *            用户名
	 */
	public void authParticipant(Integer participantNum, String participantName);

	/**
	 * 查询所有的参与人员信息。
	 * 
	 * @return
	 */
	public List<Participant> queryAllParticipant();

}
