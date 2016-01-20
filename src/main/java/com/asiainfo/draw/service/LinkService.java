package com.asiainfo.draw.service;

import java.util.List;

import com.asiainfo.draw.domain.DrawLink;
import com.asiainfo.draw.domain.LinkItem;
import com.asiainfo.draw.domain.WinningRecord;

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
	 * ��ȡ��ǰ�����û��н���¼
	 * 
	 * @return
	 */
	List<WinningRecord> getCurrnetLinkHitPrize();

	/**
	 * ��ȡ��ǰ����
	 * 
	 * @return
	 * 
	 */
	DrawLink getCurrentLink();

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
