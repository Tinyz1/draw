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
		// 1�������ֻ������ȡ�û���Ϣ

		// 2���жϵ�ǰ�����Ƿ�������û�����

		// 3�������ǰ����ֻ��δ�н����û����ţ���ô��ǰ�û�������齱

		// 4����ȡ��ǰ����ĳ齱��

		// 5����ʼ�齱

		// 6�����س齱���

		return null;
	}

}
