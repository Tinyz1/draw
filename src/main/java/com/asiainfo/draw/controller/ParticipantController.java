package com.asiainfo.draw.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public DefaultResult add(String participants) {
		DefaultResult result = null;
		try {
			participantService.add(participants);
			result = DefaultResult.newSuccessInstance(1, "参与人员添加成功！");
		} catch (Exception e) {
			logger.error(e.toString());
			result = DefaultResult.newErrorInstance(1, "参与人员添加失败！");
		}
		logger.info(result.toString());
		return result;
	}

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
		logger.info(result.toString());
		return result;
	}

	/**
	 * 获取可进行抽人的人员。用于人员抽取
	 * 
	 * @return
	 */
	@RequestMapping("/current/participants")
	@ResponseBody
	public List<Participant> getCurrentlinkParticipant() {
		return participantService.getCurrentlinkParticipant();
	}

	/**
	 * 获取当前环节可进行抽奖的人员。用于人员展示
	 */
	@RequestMapping("/current/pickParticipants")
	@ResponseBody
	public List<Participant> getCurrentPickParticipant() {
		return participantService.getCurrentPickParticipant();
	}

	/**
	 * 把摇中的人员加入当前可摇奖的人员中
	 * 
	 * @param ids
	 *            人员id,人员id,...
	 * @return
	 */
	@RequestMapping("/current/addPickParticipant")
	@ResponseBody
	public DefaultResult addPickParticipant(String ids) {
		DefaultResult result = null;
		try {
			participantService.addPickParticipant(ids);
			result = DefaultResult.newSuccessInstance(1, "人员添加成功！");
		} catch (AuthenticationExceptioin e) {
			logger.error(e.toString());
			result = DefaultResult.newErrorInstance(1, e.getMessage());
		} catch (Exception e) {
			logger.error(e.toString());
			result = DefaultResult.newErrorInstance(2, "人员添加失败！");
		}
		logger.info(result.toString());
		return result;
	}
}
