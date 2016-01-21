package com.asiainfo.draw.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.draw.exception.AuthenticationExceptioin;
import com.asiainfo.draw.service.CenterService;
import com.asiainfo.draw.util.Command;
import com.asiainfo.draw.util.DefaultResult;

@Controller
@RequestMapping("/center")
public class CenterController {

	private final Logger logger = LoggerFactory.getLogger(CenterController.class);

	@Autowired
	private CenterService centerService;

	@RequestMapping(value = "/home.html")
	public String home() {
		return "center";
	}

	@RequestMapping(value = "/draw.html")
	public String draw() {
		return "draw";
	}

	@RequestMapping(value = "/linkCtr.html")
	public String linkCtr() {
		return "linkCtr";
	}

	@RequestMapping(value = "/linkSet.html")
	public String linkSet() {
		return "linkSet";
	}

	@RequestMapping(value = "/user.html")
	public String user() {
		return "user";
	}

	@RequestMapping(value = "/prizeuser.html")
	public String prizeuser() {
		return "prizeuser";
	}

	@RequestMapping(value = "/person.html")
	public String person() {
		return "person";
	}

	@RequestMapping(value = "/pick/num")
	@ResponseBody
	public DefaultResult pickNum(Integer partnum) {
		DefaultResult result = null;
		try {
			centerService.pickNum(partnum);
			result = DefaultResult.newSuccessInstance(1, "启动选人环节成功！");
		} catch (AuthenticationExceptioin e) {
			logger.error(e.toString());
			result = DefaultResult.newErrorInstance(1, e.getMessage());
		} catch (Exception e) {
			logger.error(e.toString());
			result = DefaultResult.newErrorInstance(2, "启动选人环节失败！");
		}
		logger.info(result.toString());
		return result;
	}

	@RequestMapping("/getCommand/{identity}")
	@ResponseBody
	public Command getCommand(@PathVariable String identity) {
		return centerService.getCommand(identity);
	}

	/**
	 * 一键抽奖
	 * 
	 * @return
	 */
	@RequestMapping("/manual")
	@ResponseBody
	public DefaultResult manual() {
		DefaultResult result = null;
		try {
			centerService.manual();
			result = DefaultResult.newSuccessInstance(1, "抽奖成功！");
		} catch (AuthenticationExceptioin e) {
			logger.error(e.toString());
			result = DefaultResult.newErrorInstance(1, e.getMessage());
		} catch (Exception e) {
			logger.error(e.toString());
			result = DefaultResult.newErrorInstance(2, "抽奖失败！");
		}
		logger.info(result.toString());
		return result;
	}

	@RequestMapping(value = "/pick/start")
	@ResponseBody
	public DefaultResult pickStart() {
		DefaultResult result = null;
		try {
			centerService.startPickNum();
			result = DefaultResult.newSuccessInstance(1, "开始成功！");
		} catch (AuthenticationExceptioin e) {
			logger.error(e.toString());
			result = DefaultResult.newErrorInstance(1, e.getMessage());
		} catch (Exception e) {
			logger.error(e.toString());
			result = DefaultResult.newErrorInstance(2, "开始失败！");
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
			result = DefaultResult.newSuccessInstance(1, "结束选人成功！");
		} catch (AuthenticationExceptioin e) {
			logger.error(e.toString());
			result = DefaultResult.newErrorInstance(1, e.getMessage());
		} catch (Exception e) {
			logger.error(e.toString());
			result = DefaultResult.newErrorInstance(2, "结束选人失败！");
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
			result = DefaultResult.newSuccessInstance(1, "提交选人成功！");
		} catch (AuthenticationExceptioin e) {
			logger.error(e.toString());
			result = DefaultResult.newErrorInstance(1, e.getMessage());
		} catch (Exception e) {
			logger.error(e.toString());
			result = DefaultResult.newErrorInstance(2, "提交选人失败！");
		}
		logger.info(result.toString());
		return result;
	}

}
