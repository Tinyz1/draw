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
		// ����δ��ʼʱ���������
		LinkState linkState = (LinkState) currentLinkCache.get(CurrentLinkCache.CURRENT_STATE);
		if (!LinkState.INIT.equals(linkState)) {
			throw new RuntimeException("�����ѿ�ʼ�����������");
		}
		logger.info("<<====================ѡ�������Ա����:{}", partnum);
		// ��ת��ѡ�˽���
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
		// ��ʼ������
		linkService.initPool();
		// �����齱
		linkService.startCurrentLink();
		// ��ת���н�չʾ����
		redirectCache.put(CommandCache.CURRENT_COMMAND, Command.redirect("LuckyList.jsp"));
	}

}
