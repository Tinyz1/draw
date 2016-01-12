package com.asiainfo.draw.service;

import java.util.List;

import com.asiainfo.draw.domain.DrawLink;
import com.asiainfo.draw.util.ParticipantPrize;

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

	/**
	 * 初始化奖池
	 */
	void initPool();

	/**
	 * 获取环节的用户中奖记录
	 * 
	 * @param linkId
	 *            环节ID
	 * @return 用户中奖记录
	 */
	List<ParticipantPrize> getLinkHitPrize(Integer linkId);

}
