package com.asiainfo.draw.service;

import java.util.List;

import com.asiainfo.draw.domain.DrawLink;
import com.asiainfo.draw.domain.LinkItem;
import com.asiainfo.draw.util.ParticipantPrize;

/**
 * 环节服务接口
 * 
 * @author yecl
 *
 */
public interface LinkService {
	/**
	 * 结束环节
	 */
	void finishLink(Integer linkId);

	/**
	 * 初始化环节
	 * 
	 * @param linkId
	 *            环节id
	 */
	void initLink(Integer linkId);

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

	/**
	 * 获取当前环节
	 * 
	 * @return
	 * 
	 */
	DrawLink getCurrentLink();

	/**
	 * 验证环节编号是否正确
	 * 
	 * @param enterNmuber
	 *            环节编号
	 */
	void authLinkNumber(String enterNmuber);

	/**
	 * 获取所有环节
	 * 
	 * @return
	 */
	List<DrawLink> getAll();

	/**
	 * 初始化新的环节
	 * 
	 * @param linkId
	 *            环节ID
	 */
	void resetLink(Integer linkId);

	/**
	 * 添加新的抽奖环节
	 * 
	 * @param item
	 */
	void add(LinkItem item);

}
