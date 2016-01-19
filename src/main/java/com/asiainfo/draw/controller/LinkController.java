package com.asiainfo.draw.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.draw.domain.DrawLink;
import com.asiainfo.draw.domain.LinkItem;
import com.asiainfo.draw.exception.StartLinkException;
import com.asiainfo.draw.service.LinkService;
import com.asiainfo.draw.util.DefaultResult;
import com.asiainfo.draw.util.ParticipantPrize;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 环节控制器
 * 
 * @author yecl
 *
 */
@Controller
@RequestMapping("/link")
public class LinkController {

	private Logger logger = LoggerFactory.getLogger(LinkController.class);

	@Autowired
	private LinkService linkService;

	@RequestMapping("/add")
	@ResponseBody
	public DefaultResult add(String linkItem) {
		DefaultResult result = null;
		try {
			logger.info(linkItem);
			ObjectMapper mapper = new ObjectMapper();
			LinkItem item = mapper.readValue(linkItem, LinkItem.class);
			linkService.add(item);
			result = DefaultResult.newSuccessInstance(1, "环节添加成功！");
		} catch (Exception e) {
			logger.error(e.toString());
			result = DefaultResult.newErrorInstance(2, "环节添加失败！");
		}
		return result;
	}

	@RequestMapping("/finish")
	@ResponseBody
	public DefaultResult finish(Integer linkId) {
		DefaultResult result = null;
		try {
			linkService.finishLink(linkId);
			result = DefaultResult.newSuccessInstance(1, "环节已经结束！");
		} catch (StartLinkException e) {
			result = DefaultResult.newErrorInstance(1, e.getMessage());
		} catch (Exception e) {
			result = DefaultResult.newErrorInstance(2, "环节结束失败！");
		}
		return result;
	}

	/**
	 * 初始化新的环节
	 * 
	 * @param linkId
	 *            环节ID
	 * @return
	 */
	@RequestMapping("/init")
	@ResponseBody
	public DefaultResult init(Integer linkId) {
		DefaultResult result = null;
		try {
			linkService.initLink(linkId);
			result = DefaultResult.newSuccessInstance(1, "环节初始完毕");
		} catch (StartLinkException e) {
			result = DefaultResult.newErrorInstance(1, e.getMessage());
		} catch (Exception e) {
			result = DefaultResult.newErrorInstance(2, "环节初始化失败！");
		}
		return result;
	}

	@RequestMapping("/reset")
	@ResponseBody
	public DefaultResult reset(Integer linkId) {
		DefaultResult result = null;
		try {
			linkService.resetLink(linkId);
			result = DefaultResult.newSuccessInstance(1, "环节重置完毕！");
		} catch (StartLinkException e) {
			result = DefaultResult.newErrorInstance(1, e.getMessage());
		} catch (Exception e) {
			result = DefaultResult.newErrorInstance(2, "环节重置失败！");
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
	@RequestMapping("/currentHits")
	@ResponseBody
	public List<ParticipantPrize> getCurrnetLinkHitPrize() {
		return linkService.getCurrnetLinkHitPrize();
	}

	/**
	 * 获取当前环节
	 * 
	 * @return
	 */
	@RequestMapping("/current")
	@ResponseBody
	public DrawLink current() {
		DrawLink link = null;
		try {
			link = linkService.getCurrentLink();
		} catch (Exception e) {
			link = new DrawLink();
			link.setLinkName("未开始任何环节！");
			logger.error(e.toString());
		}
		return link;
	}

	/**
	 * 获取所有环节
	 * 
	 * @return
	 */
	@RequestMapping("/all")
	@ResponseBody
	public List<DrawLink> getAllLink() {
		List<DrawLink> links = linkService.getAll();
		//logger.info(links.toString());
		return links;
	}

}
