package com.asiainfo.draw.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.draw.domain.Participant;
import com.asiainfo.draw.exception.AuthenticationExceptioin;
import com.asiainfo.draw.service.ParticipantService;
import com.asiainfo.draw.util.DefaultResult;

/**
 * 参与人员控制器
 * 
 * @author yecl
 *
 */
@Controller
@RequestMapping("/participant")
public class ParticipantController {

	private static final Logger logger = LoggerFactory.getLogger(ParticipantController.class);

	@Autowired
	private ParticipantService participantService;

	@RequestMapping(value = "/auth", method = RequestMethod.POST)
	@ResponseBody
	public DefaultResult authParticipantByTelphone(String participantName) {
		DefaultResult result = null;
		try {
			participantService.authParticipant(participantName);
			result = DefaultResult.newSuccessInstance(1, "身份验证成功！");
		} catch (AuthenticationExceptioin e) {
			logger.error(e.toString());
			result = DefaultResult.newErrorInstance(1, e.getMessage());
		} catch (Exception e) {
			logger.error(e.toString());
			result = DefaultResult.newErrorInstance(2, "身份验证失败！");
		}
		return result;
	}

	@RequestMapping("/{participantId}")
	@ResponseBody
	public Participant authParticipantByTelphone(@PathVariable Integer participantId) {
		logger.debug("<<--" + participantId);
		return participantService.getByParticipantId(participantId);
	}
}
