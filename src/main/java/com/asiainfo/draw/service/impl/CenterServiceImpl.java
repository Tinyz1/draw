package com.asiainfo.draw.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asiainfo.draw.cache.CommandCache;
import com.asiainfo.draw.cache.CurrentLinkCache;
import com.asiainfo.draw.service.CenterService;
import com.asiainfo.draw.util.Command;

@Service("centerService")
@Transactional
public class CenterServiceImpl implements CenterService {

	@Autowired
	private CurrentLinkCache currentLinkCache;

	@Autowired
	private CommandCache redirectCache;

	@Override
	public void pickNum(Integer partnum) {
		currentLinkCache.put(CurrentLinkCache.CURRENT_PICK_NUM, partnum);
		Command command = new Command();
		command.setType(Command.COMMAND_REDIRECT);
		command.setUrl("/draw/luckPerson.jsp?radom=" + partnum);
		redirectCache.put(CommandCache.CURRENT_COMMAND, command);
	}

	@Override
	public Command getCommand() {
		Command command = redirectCache.get(CommandCache.CURRENT_COMMAND);
		redirectCache.invalidate();
		return command;
	}

	@Override
	public void startPickNum() {
		Command command = new Command();
		command.setType(Command.ACTION_PICK_START);
		redirectCache.put(CommandCache.CURRENT_COMMAND, command);
	}

	@Override
	public void endPickNum() {
		Command command = new Command();
		command.setType(Command.ACTION_PICK_END);
		redirectCache.put(CommandCache.CURRENT_COMMAND, command);
	}

	@Override
	public void commitPicNum() {
		Command command = new Command();
		command.setType(Command.ACTION_INIT_POOL);
		redirectCache.put(CommandCache.CURRENT_COMMAND, command);
	}

}
