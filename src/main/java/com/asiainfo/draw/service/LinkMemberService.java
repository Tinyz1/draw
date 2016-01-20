package com.asiainfo.draw.service;

import java.util.List;

import com.asiainfo.draw.domain.LinkMember;

/**
 * 环节参与人品服务
 * 
 * @author yecl
 *
 */
public interface LinkMemberService {

	/**
	 * 根据环节ID和状态获取环节参与人员。
	 * 
	 * @param linkId
	 *            环节ID
	 * @param state
	 *            状态。状态为空时，查询所有状态的参与人员
	 * @return 环节参与人员
	 */
	List<LinkMember> getMemberByLinkIdAndState(Integer linkId, Integer state);

	/**
	 * 创建环节参与人员
	 * 
	 * @param members
	 */
	void create(LinkMember... members);

	/**
	 * 创建环节参与人员
	 * 
	 * @param members
	 */
	void create(List<LinkMember> members);

	/**
	 * 确认环节参与人员
	 * 
	 * @param members
	 */
	void confirm(LinkMember... members);

	/**
	 * 确认环节参与人员
	 * 
	 * @param members
	 */
	void confirm(List<LinkMember> members);

	/**
	 * 判断用户是否在某个环节中。
	 * 
	 * @param linkId
	 *            环节ID
	 * @param participantId
	 *            用户ID
	 * @param state
	 *            状态。如果为空，表示所有的状态
	 * @return
	 */
	boolean isLinkContainMember(Integer linkId, Integer participantId, Integer state);

}
