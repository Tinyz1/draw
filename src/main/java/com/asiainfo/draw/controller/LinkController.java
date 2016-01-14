package com.asiainfo.draw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.draw.domain.DrawLink;
import com.asiainfo.draw.exception.StartLinkException;
import com.asiainfo.draw.service.LinkService;
import com.asiainfo.draw.util.DefaultResult;
import com.asiainfo.draw.util.ParticipantPrize;

/**
 * 环节控制器
 * 
 * @author yecl
 *
 */
@Controller
@RequestMapping("/link")
public class LinkController {

	@Autowired
	private LinkService linkService;
	
	/**
	 * 
	 * @param enterNmuber
	 * @return
	 */
	@RequestMapping("/authLinkNumber")
	@ResponseBody
	public DefaultResult auth(String enterNmuber) {
		DefaultResult result = null;
		try {
			linkService.authLinkNumber(enterNmuber);
			result = DefaultResult.newSuccessInstance(1, "环节编号验证成功！");
		} catch (StartLinkException e) {
			result = DefaultResult.newErrorInstance(1, e.getMessage());
		} catch (Exception e) {
			result = DefaultResult.newErrorInstance(2, "环节编号验证失败！");
		}
		return result;
	}

	@RequestMapping("/start")
	@ResponseBody
	public DefaultResult start() {
		DefaultResult result = null;
		try {
			linkService.startCurrentLink();
			result = DefaultResult.newSuccessInstance(1, "已经启动新的环节，可以抽奖了。");
		} catch (StartLinkException e) {
			result = DefaultResult.newErrorInstance(1, e.getMessage());
		} catch (Exception e) {
			result = DefaultResult.newErrorInstance(2, "环节启动失败！");
		}
		return result;
	}

	@RequestMapping("/initPool")
	@ResponseBody
	public DefaultResult initPool() {
		DefaultResult result = null;
		try {
			linkService.initPool();
			result = DefaultResult.newSuccessInstance(1, "奖池已经初始化完毕，可以开始抽奖了。");
		} catch (StartLinkException e) {
			result = DefaultResult.newErrorInstance(1, e.getMessage());
		} catch (Exception e) {
			result = DefaultResult.newErrorInstance(2, "奖池已经初始化失败！");
		}
		return result;
	}

	/**
	 * 获取环节的用户中奖记录
	 * 
	 * @param linkId
	 *            环节ID
	 * @return
	 */
	@RequestMapping("/hitPrize")
	@ResponseBody
	public List<ParticipantPrize> getLinkHitPrize(Integer linkId) {
		return linkService.getLinkHitPrize(linkId);
	}

	/**
	 * 获取当前环节
	 * 
	 * @return
	 */
	@RequestMapping("/current")
	@ResponseBody
	public DrawLink current() {
		return linkService.getCurrentLink();
	}

}
