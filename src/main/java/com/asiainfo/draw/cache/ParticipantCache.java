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
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.asiainfo.draw.domain.Participant;
import com.asiainfo.draw.service.ParticipantService;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

@Component
@Scope("singleton")
public class ParticipantCache implements InitializingBean {

	private final Logger logger = LoggerFactory.getLogger(ParticipantCache.class);

	private final Cache<String, Participant> cache = CacheBuilder.newBuilder().build();

	public synchronized void put(Integer key, Participant value) {
		logger.info("д�뻺�棬key: {}, value: {}", key, value);
		cache.put(String.valueOf(key), value);
	}

	public Participant get(final Integer key) {
		try {
			Participant participant = (Participant) cache.get(String.valueOf(key), new Callable<Participant>() {
				@Override
				public Participant call() throws Exception {
					logger.info("<<============ִ������");
					Participant selParticipant = participantService.getByParticipantNum(key);
					if (selParticipant != null) {
						put(key, selParticipant);
						return selParticipant;
					}
					return null;
				}
			});
			logger.info("����key: {}����ȡ��������Ա��Ϣ value: {}", key, participant);
			return participant;
		} catch (ExecutionException e) {

		}
		return null;
	}

	@Autowired
	private ParticipantService participantService;

	private static int i = 0;

	public List<Participant> getAll() {
		ConcurrentMap<String, Participant> maps = cache.asMap();

		List<Participant> participants = new ArrayList<Participant>();
		for (Map.Entry<String, Participant> map : maps.entrySet()) {
			participants.add(map.getValue());
		}

		return participants;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (++i > 1) {
			return;
		}
		logger.info("<<==============��ʼ��ʼ��������Ա��Ϣ");
		logger.info("<<==============��ѯ���ݿ�����Ϣ");
		List<Participant> participants = participantService.queryAllParticipant();

		if (participants != null && participants.size() > 0) {
			for (Participant participant : participants) {
				put(participant.getParticipantNum(), participant);
			}
		}
		logger.info("<<==============������Ա��Ϣ�������");
	}

}
