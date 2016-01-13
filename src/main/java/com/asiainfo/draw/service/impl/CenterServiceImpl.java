package com.asiainfo.draw.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asiainfo.draw.cache.AllPickCache;
import com.asiainfo.draw.cache.CommandCache;
import com.asiainfo.draw.cache.CurrentLinkCache;
import com.asiainfo.draw.domain.Participant;
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
		logger.info("<<====================选择参与人员数量:{}", partnum);
		// 获取当前可被参与抽人环节的人数
		List<Participant> participants = allPickCache.getAll();
		int allow = participants.size();

		currentLinkCache.put(CurrentLinkCache.CURRENT_PICK_NUM, partnum);
		Command command = new Command();
		command.setType(Command.COMMAND_REDIRECT);
		command.setUrl("/draw/luckPerson.jsp?partnum=" + partnum + "&allow=" + allow);
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
