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
 * ���ڿ�����
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
			result = DefaultResult.newSuccessInstance(1, "���ڱ����֤�ɹ���");
		} catch (StartLinkException e) {
			result = DefaultResult.newErrorInstance(1, e.getMessage());
		} catch (Exception e) {
			result = DefaultResult.newErrorInstance(2, "���ڱ����֤ʧ�ܣ�");
		}
		return result;
	}

	@RequestMapping("/start")
	@ResponseBody
	public DefaultResult start() {
		DefaultResult result = null;
		try {
			linkService.startCurrentLink();
			result = DefaultResult.newSuccessInstance(1, "�Ѿ������µĻ��ڣ����Գ齱�ˡ�");
		} catch (StartLinkException e) {
			result = DefaultResult.newErrorInstance(1, e.getMessage());
		} catch (Exception e) {
			result = DefaultResult.newErrorInstance(2, "��������ʧ�ܣ�");
		}
		return result;
	}

	@RequestMapping("/initPool")
	@ResponseBody
	public DefaultResult initPool() {
		DefaultResult result = null;
		try {
			linkService.initPool();
			result = DefaultResult.newSuccessInstance(1, "�����Ѿ���ʼ����ϣ����Կ�ʼ�齱�ˡ�");
		} catch (StartLinkException e) {
			result = DefaultResult.newErrorInstance(1, e.getMessage());
		} catch (Exception e) {
			result = DefaultResult.newErrorInstance(2, "�����Ѿ���ʼ��ʧ�ܣ�");
		}
		return result;
	}

	/**
	 * ��ȡ���ڵ��û��н���¼
	 * 
	 * @param linkId
	 *            ����ID
	 * @return
	 */
	@RequestMapping("/hitPrize")
	@ResponseBody
	public List<ParticipantPrize> getLinkHitPrize(Integer linkId) {
		return linkService.getLinkHitPrize(linkId);
	}

	/**
	 * ��ȡ��ǰ����
	 * 
	 * @return
	 */
	@RequestMapping("/current")
	@ResponseBody
	public DrawLink current() {
		return linkService.getCurrentLink();
	}

}
