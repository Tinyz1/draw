package com.asiainfo.draw.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.draw.domain.Participant;
import com.asiainfo.draw.service.ParticipantService;

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

	@RequestMapping("/auth")
	@ResponseBody
	public Participant authParticipantByTelphone(Participant participant) {
		logger.debug("<<--" + participant.toString());
		participant = participantService.authParticipant(participant);
		return participant;
	}
	
	@RequestMapping("/{participantId}")
	@ResponseBody
	public Participant authParticipantByTelphone(@PathVariable Integer participantId) {
		logger.debug("<<--" + participantId);
		return participantService.getByParticipantId(participantId);
	}
}
