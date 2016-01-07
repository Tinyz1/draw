package com.asiainfo.draw.service.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asiainfo.draw.persistence.ParticipantMapper;
import com.asiainfo.draw.service.DrawService;
import com.asiainfo.draw.util.Draw.Prize;

@Service("drawService")
@Transactional
public class DrawServiceImpl implements DrawService {

	@Autowired
	private ParticipantMapper participantMapper;

	@Override
	public Prize pick(Integer roomId, String phone) {
		checkNotNull(roomId);
		checkNotNull(phone);
		// 1、根据手机号码获取用户信息

		// 2、判断当前环节是否对所有用户开放

		// 3、如果当前环节只对未中奖的用户开放，那么当前用户不参与抽奖

		// 4、获取当前房间的抽奖池

		// 5、开始抽奖

		// 6、返回抽奖结果

		return null;
	}

}
