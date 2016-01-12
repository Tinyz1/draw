package com.asiainfo.draw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.draw.exception.StartLinkException;
import com.asiainfo.draw.service.LinkService;
import com.asiainfo.draw.util.DefaultResult;

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

}
