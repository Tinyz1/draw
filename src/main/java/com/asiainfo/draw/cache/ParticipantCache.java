package com.asiainfo.draw.cache;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.asiainfo.draw.domain.Participant;
import com.asiainfo.draw.service.ParticipantService;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * ������Ա����
 * 
 * @author yecl
 *
 */
@Component
public class ParticipantCache implements InitializingBean {

	private final Logger logger = LoggerFactory.getLogger(ParticipantCache.class);

	/**
	 * ����ID��ֵ��
	 */
	private Cache<Integer, Participant> idCache = CacheBuilder.newBuilder().build();

	/**
	 * �������Ƽ�ֵ��
	 */
	private Cache<String, Participant> nameCache = CacheBuilder.newBuilder().build();

	@Autowired
	private ParticipantService participantService;

	public synchronized void put(Integer id, Participant value) {
		logger.info("д�뻺��->������ԱID: {}, ������Ա: {}", id, value);
		idCache.put(id, value);
	}

	public synchronized void put(String name, Participant value) {
		logger.info("д�뻺��->������Ա����: {}, ������Ա: {}", name, value);
		nameCache.put(name, value);
	}

	public Participant reload(final Integer id) {
		logger.info("���¼��ػ���->������ԱID:{}", id);
		idCache.invalidate(id);
		return get(id);
	}

	public Participant reload(final String name) {
		logger.info("���¼��ػ���->������Ա����:{}", name);
		nameCache.invalidate(name);
		return get(name);
	}

	public Participant get(final Integer id) {
		try {
			logger.info("��ȡ�û�����->������ԱID:{}", id);
			Participant participant = (Participant) idCache.get(id, new Callable<Participant>() {
				@Override
				public Participant call() throws Exception {
					logger.info("��ѯ���ݿ�->�û�ID:{}");
					Participant selParticipant = participantService.getByParticipantId(id);
					if (selParticipant != null) {
						put(id, selParticipant);
						put(selParticipant.getParticipantName(), selParticipant);
					}
					return selParticipant;
				}
			});

			return participant;

		} catch (ExecutionException e) {
			logger.error(e.toString());
		}
		return null;
	}

	public Participant get(final String name) {
		try {
			logger.info("��ȡ�û�����->������Ա����:{}", name);
			Participant participant = (Participant) nameCache.get(name, new Callable<Participant>() {
				@Override
				public Participant call() throws Exception {
					logger.info("��ѯ���ݿ�->�û�����:{}");
					Participant selParticipant = participantService.getByParticipantName(name);
					if (selParticipant != null) {
						put(name, selParticipant);
						put(selParticipant.getParticipantId(), selParticipant);
					}
					return selParticipant;
				}
			});
			return participant;
		} catch (ExecutionException e) {
			logger.error(e.toString());
		}
		return null;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		load();
	}

	/**
	 * ���û���Ϣ���ص�������
	 */
	public void load() {
		logger.info("��ʼ�����û�����->");
		List<Participant> participants = participantService.queryAllParticipant();
		if (participants != null && participants.size() > 0) {
			for (Participant participant : participants) {
				if (participant != null) {
					put(participant.getParticipantId(), participant);
					put(participant.getParticipantName(), participant);
				}
			}
		}
		logger.info("�����û��������->");
	}

	/**
	 * ���¼����û���Ϣ��������
	 */
	public void reload() {
		logger.info("���¼����û�����->");
		idCache.invalidateAll();
		nameCache.invalidateAll();
		load();
	}

}
