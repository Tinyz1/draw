package com.asiainfo.draw.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.draw.service.DrawService;
import com.asiainfo.draw.util.Prize;

@Controller
@RequestMapping("/draw")
public class DrawController {

	private final Logger logger = LoggerFactory.getLogger(DrawController.class);

	@Autowired
	private DrawService drawService;

	@RequestMapping("/pick")
	@ResponseBody
	public Prize pick(String participantName) {
		logger.debug("participantName: " + participantName);
		Prize prize = null;
		try {
			prize = drawService.pick(participantName);
		} catch (Exception e) {
			logger.error("ϵͳ���󣬴�����Ϣ��{}", e);
			prize = Prize.createOverPrize();
		}
		return prize;
	}

}
