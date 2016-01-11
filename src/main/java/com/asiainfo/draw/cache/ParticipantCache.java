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

	public synchronized void put(String key, Participant value) {
		logger.info("写入缓存，key: {}, value: {}", key, value);
		cache.put(key, value);
	}

	public Participant get(final String key) {
		try {
			Participant participant = (Participant) cache.get(key, new Callable<Participant>() {
				@Override
				public Participant call() throws Exception {
					logger.info("<<============执行了吗？");
					Participant selParticipant = participantService.getByParticipantName(key);
					if (selParticipant != null) {
						put(key, selParticipant);
						return selParticipant;
					}
					return null;
				}
			});
			logger.info("根据key: {}，获取到参与人员信息 value: {}", key, participant);
			return participant;
		} catch (ExecutionException e) {

		}
		return null;
	}

	@Autowired
	private ParticipantService participantService;

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
		logger.info("<<==============开始初始化参与人员信息");
		List<Participant> participants = participantService.queryAllParticipant();
		if (participants != null && participants.size() > 0) {
			for (Participant participant : participants) {
				logger.info("<<======用户：{}加入缓存...", participant.getParticipantName());
				put(participant.getParticipantName(), participant);
			}
		}
		logger.info("<<==============参与人员信息加载完毕");
	}

}
