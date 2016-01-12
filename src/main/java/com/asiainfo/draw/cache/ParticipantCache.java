package com.asiainfo.draw.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
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
		logger.info("д�뻺�棬key: {}, value: {}", id, value);
		idCache.put(id, value);
	}

	public synchronized void put(String name, Participant value) {
		logger.info("д�뻺�棬key: {}, value: {}", name, value);
		nameCache.put(name, value);
	}

	public Participant get(final Integer id) {
		try {
			logger.info("�����û�ID:{}�ӻ����л�ȡ�û���Ϣ...", id);
			Participant participant = (Participant) idCache.get(id, new Callable<Participant>() {
				@Override
				public Participant call() throws Exception {
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
			logger.info("�����û�����:{}�ӻ����л�ȡ�û���Ϣ...", name);
			Participant participant = (Participant) nameCache.get(name, new Callable<Participant>() {
				@Override
				public Participant call() throws Exception {
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

	public List<Participant> getAll() {
		ConcurrentMap<String, Participant> maps = nameCache.asMap();
		List<Participant> participants = new ArrayList<Participant>();
		for (Map.Entry<String, Participant> map : maps.entrySet()) {
			participants.add(map.getValue());
		}
		return participants;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		load();
	}

	/**
	 * ���û���Ϣ���ص�������
	 */
	public void load() {
		logger.info("<<---------��ʼ�����û���Ϣ��������....");
		List<Participant> participants = participantService.queryAllParticipant();
		if (participants != null && participants.size() > 0) {
			for (Participant participant : participants) {
				if (participant != null) {
					logger.info("<<--------------�û���{}���뻺����...", participant);
					put(participant.getParticipantId(), participant);
					put(participant.getParticipantName(), participant);
				}
			}
		}
		logger.info("<<---------�����û���Ϣ����....");
	}

	/**
	 * ���¼����û���Ϣ��������
	 */
	public void reload() {
		idCache = CacheBuilder.newBuilder().build();
		nameCache = CacheBuilder.newBuilder().build();
		load();
	}

}
