package com.asiainfo.draw.service;

import java.util.List;

import com.asiainfo.draw.domain.DrawLink;
import com.asiainfo.draw.util.ParticipantPrize;

/**
 * ���ڷ���ӿ�
 * 
 * @author yecl
 *
 */
public interface LinkService {

	DrawLink nextLink();

	/**
	 * ������ǰ����
	 */
	void finishCurrentLink();

	/**
	 * ��ʼ����һ����Ϊ��ǰ����
	 */
	void initNextLink();

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

}
