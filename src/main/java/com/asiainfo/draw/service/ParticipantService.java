package com.asiainfo.draw.service;

import java.util.List;
import java.util.Set;

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
	 * 验证
	 * 
	 * @param participantName
	 * @param enterNum
	 */
	public void auth(String participantName, String enterNum);

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

	public void add(String participants);

	/**
	 * 选择的用户抽奖机会减少1次
	 * 
	 * @param participants
	 */
	void subShakeTime(Set<Participant> participants);

	/**
	 * 查询抽奖机会大于1的用户
	 * 
	 * @return
	 */
	public List<Participant> queryAllParticipant();

}
