package com.asiainfo.draw.service;

import com.asiainfo.draw.domain.DrawLink;

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

}
