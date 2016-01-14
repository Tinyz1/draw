package com.asiainfo.draw.service.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asiainfo.draw.cache.AllPickCache;
import com.asiainfo.draw.cache.CommandCache;
import com.asiainfo.draw.cache.CurrentLinkCache;
import com.asiainfo.draw.cache.CurrentLinkCache.LinkState;
import com.asiainfo.draw.service.CenterService;
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
	private AllPickCache allPickCache;

	@Override
	public void pickNum(Integer partnum) {
		checkNotNull(partnum);

		// 环节未开始时才允许加人
		LinkState linkState = (LinkState) currentLinkCache.get(CurrentLinkCache.CURRENT_STATE);
		if (!LinkState.INIT.equals(linkState)) {
			throw new RuntimeException("环节已开始，不允许加人");
		}

		logger.info("<<====================选择参与人员数量:{}", partnum);
		// 获取当前可被参与抽人环节的人数
		int allow = allPickCache.getAll().size();

		if (allow < partnum) {
			throw new RuntimeException("当前环节最大参与人数：" + allow);
		}

		int remainNum = (Integer) currentLinkCache.get(CurrentLinkCache.CURRENT_REMAIN_NUM);
		currentLinkCache.put(CurrentLinkCache.CURRENT_REMAIN_NUM, remainNum + partnum);

		Command command = new Command();
		command.setType(Command.COMMAND_REDIRECT);
		command.setUrl("luckPerson.jsp?partnum=" + partnum + "&allow=" + allow);
		commandCache.put(CommandCache.CURRENT_COMMAND, command);
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
		Command command = new Command();
		command.setType(Command.ACTION_INIT_POOL);
		commandCache.put(CommandCache.CURRENT_COMMAND, command);
	}

}
