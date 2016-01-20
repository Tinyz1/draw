package com.asiainfo.draw.service.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asiainfo.draw.cache.CommandCache;
import com.asiainfo.draw.cache.CurrentLinkCache;
import com.asiainfo.draw.cache.CurrentLinkCache.LinkState;
import com.asiainfo.draw.service.CenterService;
import com.asiainfo.draw.service.LinkService;
import com.asiainfo.draw.util.Command;

@Service("centerService")
@Transactional
public class CenterServiceImpl implements CenterService {

	private final Logger logger = LoggerFactory.getLogger(CenterServiceImpl.class);

	@Autowired
	private CurrentLinkCache currentLinkCache;

	@Autowired
	private CommandCache commandCache;

	@Autowired
	private LinkService linkService;

	@Autowired
	private CommandCache redirectCache;

	@Override
	public void pickNum(Integer partnum) {
		checkNotNull(partnum);
		// 环节未开始时才允许加人
		LinkState linkState = (LinkState) currentLinkCache.get(CurrentLinkCache.CURRENT_STATE);
		if (!LinkState.INIT.equals(linkState)) {
			throw new RuntimeException("环节已开始，不允许加人");
		}
		logger.info("<<====================选择参与人员数量:{}", partnum);
		// 跳转至选人界面
		commandCache.put(CommandCache.CURRENT_COMMAND, Command.redirect("luckPerson.jsp?partnum=" + partnum));
	}

	@Override
	public Command getCommand() {
		Command command = commandCache.get(CommandCache.CURRENT_COMMAND);
		commandCache.invalidate();
		return command;
	}

	@Override
	public void startPickNum() {
		Command command = new Command();
		command.setType(Command.ACTION_PICK_START);
		commandCache.put(CommandCache.CURRENT_COMMAND, command);
	}

	@Override
	public void endPickNum() {
		Command command = new Command();
		command.setType(Command.ACTION_PICK_END);
		commandCache.put(CommandCache.CURRENT_COMMAND, command);
	}

	@Override
	public void commitPicNum() {
		// 初始化奖池
		linkService.initPool();
		// 启动抽奖
		linkService.startCurrentLink();
		// 跳转至中奖展示界面
		redirectCache.put(CommandCache.CURRENT_COMMAND, Command.redirect("LuckyList.jsp"));
	}

}
