package com.asiainfo.draw.service;

import com.asiainfo.draw.domain.DrawLink;

/**
 * 环节服务接口
 * 
 * @author yecl
 *
 */
public interface LinkService {

	DrawLink nextLink();

	/**
	 * 结束当前环节
	 */
	void finishCurrentLink();

	/**
	 * 初始化下一环节为当前环节
	 */
	void initNextLink();

	/**
	 * 开始当前环节
	 */
	void startCurrentLink();

}
