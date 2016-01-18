package com.asiainfo.draw.service;

import java.util.List;

import com.asiainfo.draw.domain.DrawLink;
import com.asiainfo.draw.domain.LinkItem;
import com.asiainfo.draw.util.ParticipantPrize;

/**
 * ���ڷ���ӿ�
 * 
 * @author yecl
 *
 */
public interface LinkService {
	/**
	 * ��������
	 */
	void finishLink(Integer linkId);

	/**
	 * ��ʼ������
	 * 
	 * @param linkId
	 *            ����id
	 */
	void initLink(Integer linkId);

	/**
	 * ��ʼ��ǰ����
	 */
	void startCurrentLink();

	/**
	 * ��ʼ������
	 */
	void initPool();

	/**
	 * ��ȡ���ڵ��û��н���¼
	 * 
	 * @param linkId
	 *            ����ID
	 * @return �û��н���¼
	 */
	List<ParticipantPrize> getLinkHitPrize(Integer linkId);

	/**
	 * ��ȡ��ǰ����
	 * 
	 * @return
	 * 
	 */
	DrawLink getCurrentLink();

	/**
	 * ��֤���ڱ���Ƿ���ȷ
	 * 
	 * @param enterNmuber
	 *            ���ڱ��
	 */
	void authLinkNumber(String enterNmuber);

	/**
	 * ��ȡ���л���
	 * 
	 * @return
	 */
	List<DrawLink> getAll();

	/**
	 * ��ʼ���µĻ���
	 * 
	 * @param linkId
	 *            ����ID
	 */
	void resetLink(Integer linkId);

	/**
	 * ����µĳ齱����
	 * 
	 * @param item
	 */
	void add(LinkItem item);

}
