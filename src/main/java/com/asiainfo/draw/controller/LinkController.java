package com.asiainfo.draw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.draw.exception.StartLinkException;
import com.asiainfo.draw.service.LinkService;
import com.asiainfo.draw.util.DefaultResult;

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

}
