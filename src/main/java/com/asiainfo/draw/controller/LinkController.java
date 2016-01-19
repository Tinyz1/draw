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
 * ���ڿ�����
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
			result = DefaultResult.newSuccessInstance(1, "������ӳɹ���");
		} catch (Exception e) {
			logger.error(e.toString());
			result = DefaultResult.newErrorInstance(2, "�������ʧ�ܣ�");
		}
		return result;
	}

	@RequestMapping("/finish")
	@ResponseBody
	public DefaultResult finish(Integer linkId) {
		DefaultResult result = null;
		try {
			linkService.finishLink(linkId);
			result = DefaultResult.newSuccessInstance(1, "�����Ѿ�������");
		} catch (StartLinkException e) {
			result = DefaultResult.newErrorInstance(1, e.getMessage());
		} catch (Exception e) {
			result = DefaultResult.newErrorInstance(2, "���ڽ���ʧ�ܣ�");
		}
		return result;
	}

	/**
	 * ��ʼ���µĻ���
	 * 
	 * @param linkId
	 *            ����ID
	 * @return
	 */
	@RequestMapping("/init")
	@ResponseBody
	public DefaultResult init(Integer linkId) {
		DefaultResult result = null;
		try {
			linkService.initLink(linkId);
			result = DefaultResult.newSuccessInstance(1, "���ڳ�ʼ���");
		} catch (StartLinkException e) {
			result = DefaultResult.newErrorInstance(1, e.getMessage());
		} catch (Exception e) {
			result = DefaultResult.newErrorInstance(2, "���ڳ�ʼ��ʧ�ܣ�");
		}
		return result;
	}

	@RequestMapping("/reset")
	@ResponseBody
	public DefaultResult reset(Integer linkId) {
		DefaultResult result = null;
		try {
			linkService.resetLink(linkId);
			result = DefaultResult.newSuccessInstance(1, "����������ϣ�");
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
	@RequestMapping("/currentHits")
	@ResponseBody
	public List<ParticipantPrize> getCurrnetLinkHitPrize() {
		return linkService.getCurrnetLinkHitPrize();
	}

	/**
	 * ��ȡ��ǰ����
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
			link.setLinkName("δ��ʼ�κλ��ڣ�");
			logger.error(e.toString());
		}
		return link;
	}

	/**
	 * ��ȡ���л���
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
