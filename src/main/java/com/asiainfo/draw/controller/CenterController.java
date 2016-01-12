package com.asiainfo.draw.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.draw.exception.AuthenticationExceptioin;
import com.asiainfo.draw.service.CenterService;
import com.asiainfo.draw.util.DefaultResult;
import com.asiainfo.draw.util.Command;

@Controller
@RequestMapping("/center")
public class CenterController {

	private final Logger logger = LoggerFactory.getLogger(CenterController.class);

	@Autowired
	private CenterService centerService;

	@RequestMapping(value = "/pick/num")
	@ResponseBody
	public DefaultResult pickNum(Integer partnum) {
		DefaultResult result = null;
		try {
			centerService.pickNum(partnum);
			result = DefaultResult.newSuccessInstance(1, "����ѡ�˻��ڳɹ���");
		} catch (AuthenticationExceptioin e) {
			logger.error(e.toString());
			result = DefaultResult.newErrorInstance(1, e.getMessage());
		} catch (Exception e) {
			logger.error(e.toString());
			result = DefaultResult.newErrorInstance(2, "����ѡ�˻���ʧ�ܣ�");
		}
		logger.info(result.toString());
		return result;
	}

	@RequestMapping("/getCommand")
	@ResponseBody
	public Command getCommand() {
		return centerService.getCommand();
	}

	@RequestMapping(value = "/pick/start")
	@ResponseBody
	public DefaultResult pickStart() {
		DefaultResult result = null;
		try {
			centerService.startPickNum();
			result = DefaultResult.newSuccessInstance(1, "��ʼ�ɹ���");
		} catch (AuthenticationExceptioin e) {
			logger.error(e.toString());
			result = DefaultResult.newErrorInstance(1, e.getMessage());
		} catch (Exception e) {
			logger.error(e.toString());
			result = DefaultResult.newErrorInstance(2, "��ʼʧ�ܣ�");
		}
		logger.info(result.toString());
		return result;
	}

	@RequestMapping(value = "/pick/end")
	@ResponseBody
	public DefaultResult pickEnd() {
		DefaultResult result = null;
		try {
			centerService.endPickNum();
			result = DefaultResult.newSuccessInstance(1, "����ѡ�˳ɹ���");
		} catch (AuthenticationExceptioin e) {
			logger.error(e.toString());
			result = DefaultResult.newErrorInstance(1, e.getMessage());
		} catch (Exception e) {
			logger.error(e.toString());
			result = DefaultResult.newErrorInstance(2, "����ѡ��ʧ�ܣ�");
		}
		logger.info(result.toString());
		return result;
	}

	@RequestMapping(value = "/pick/commit")
	@ResponseBody
	public DefaultResult pickCommit() {
		DefaultResult result = null;
		try {
			centerService.commitPicNum();
			result = DefaultResult.newSuccessInstance(1, "�ύѡ�˳ɹ���");
		} catch (AuthenticationExceptioin e) {
			logger.error(e.toString());
			result = DefaultResult.newErrorInstance(1, e.getMessage());
		} catch (Exception e) {
			logger.error(e.toString());
			result = DefaultResult.newErrorInstance(2, "�ύѡ��ʧ�ܣ�");
		}
		logger.info(result.toString());
		return result;
	}

}
