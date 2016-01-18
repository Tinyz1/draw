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
 * ������Ա������
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
			result = DefaultResult.newSuccessInstance(1, "������Ա��ӳɹ���");
		} catch (Exception e) {
			logger.error(e.toString());
			result = DefaultResult.newErrorInstance(1, "������Ա���ʧ�ܣ�");
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
			result = DefaultResult.newSuccessInstance(1, "�����֤�ɹ���");
		} catch (AuthenticationExceptioin e) {
			logger.error(e.toString());
			result = DefaultResult.newErrorInstance(1, e.getMessage());
		} catch (Exception e) {
			logger.error(e.toString());
			result = DefaultResult.newErrorInstance(2, "�����֤ʧ�ܣ�");
		}
		logger.info(result.toString());
		return result;
	}

	/**
	 * ��ȡ�ɽ��г��˵���Ա��������Ա��ȡ
	 * 
	 * @return
	 */
	@RequestMapping("/current/participants")
	@ResponseBody
	public List<Participant> getCurrentlinkParticipant() {
		return participantService.getCurrentlinkParticipant();
	}

	/**
	 * ��ȡ��ǰ���ڿɽ��г齱����Ա��������Աչʾ
	 */
	@RequestMapping("/current/pickParticipants")
	@ResponseBody
	public List<Participant> getCurrentPickParticipant() {
		return participantService.getCurrentPickParticipant();
	}

	/**
	 * ��ҡ�е���Ա���뵱ǰ��ҡ������Ա��
	 * 
	 * @param ids
	 *            ��Աid,��Աid,...
	 * @return
	 */
	@RequestMapping("/current/addPickParticipant")
	@ResponseBody
	public DefaultResult addPickParticipant(String ids) {
		DefaultResult result = null;
		try {
			participantService.addPickParticipant(ids);
			result = DefaultResult.newSuccessInstance(1, "��Ա��ӳɹ���");
		} catch (AuthenticationExceptioin e) {
			logger.error(e.toString());
			result = DefaultResult.newErrorInstance(1, e.getMessage());
		} catch (Exception e) {
			logger.error(e.toString());
			result = DefaultResult.newErrorInstance(2, "��Ա���ʧ�ܣ�");
		}
		logger.info(result.toString());
		return result;
	}
}
