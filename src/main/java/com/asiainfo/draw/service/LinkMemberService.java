package com.asiainfo.draw.service;

import java.util.List;

import com.asiainfo.draw.domain.LinkMember;

/**
 * ���ڲ�����Ʒ����
 * 
 * @author yecl
 *
 */
public interface LinkMemberService {

	/**
	 * ���ݻ���ID��״̬��ȡ���ڲ�����Ա��
	 * 
	 * @param linkId
	 *            ����ID
	 * @param state
	 *            ״̬��״̬Ϊ��ʱ����ѯ����״̬�Ĳ�����Ա
	 * @return ���ڲ�����Ա
	 */
	List<LinkMember> getMemberByLinkIdAndState(Integer linkId, Integer state);

	/**
	 * �������ڲ�����Ա
	 * 
	 * @param members
	 */
	void create(LinkMember... members);

	/**
	 * �������ڲ�����Ա
	 * 
	 * @param members
	 */
	void create(List<LinkMember> members);

	/**
	 * ȷ�ϻ��ڲ�����Ա
	 * 
	 * @param members
	 */
	void confirm(LinkMember... members);

	/**
	 * ȷ�ϻ��ڲ�����Ա
	 * 
	 * @param members
	 */
	void confirm(List<LinkMember> members);

	/**
	 * �ж��û��Ƿ���ĳ�������С�
	 * 
	 * @param linkId
	 *            ����ID
	 * @param participantId
	 *            �û�ID
	 * @param state
	 *            ״̬�����Ϊ�գ���ʾ���е�״̬
	 * @return
	 */
	boolean isLinkContainMember(Integer linkId, Integer participantId, Integer state);

}
