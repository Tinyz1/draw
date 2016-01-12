package com.asiainfo.draw.service;

import java.util.List;

import com.asiainfo.draw.domain.Participant;

public interface ParticipantService {

	/**
	 * 根据用户ID获取用户信息
	 * 
	 * @param ParticipantId
	 *            用户ID
	 * @return 参与人员信息
	 */
	public Participant getByParticipantId(Integer participantId);

	/**
	 * 根据参与人员姓名获取参与人员
	 * 
	 * @param participantName
	 *            参与人员姓名
	 * @return 参与人员
	 */
	public Participant getByParticipantName(String participantName);

	/**
	 * 根据用户编号和用户名验证用户是否能够参与活动
	 * 
	 * @param participantNum
	 *            用户编号
	 * @param participantName
	 *            用户名
	 */
	public void authParticipant(String participantName);

	/**
	 * 查询所有的参与人员信息。
	 * 
	 * @return
	 */
	public List<Participant> queryAllParticipant();

	/**
	 * 获取可进行抽人的人员
	 * 
	 * @return
	 */
	public List<Participant> getCurrentlinkParticipant();

	/**
	 * 获取当前环节可进行抽奖的人员
	 * 
	 * @return
	 */
	public List<Participant> getCurrentPickParticipant();

	/**
	 * 把摇中的人员加入当前可摇奖的人员中
	 * 
	 * @param ids
	 *            人员id,人员id,...
	 */
	public void addPickParticipant(String ids);

}
