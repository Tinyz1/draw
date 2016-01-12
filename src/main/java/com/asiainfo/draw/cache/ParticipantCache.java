package com.asiainfo.draw.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.StringUtils;
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

	private final Cache<String, Participant> cache = CacheBuilder.newBuilder().build();

	@Autowired
	private ParticipantService participantService;

	public synchronized void put(String key, Participant value) {
		logger.info("写入缓存，key: {}, value: {}", key, value);
		cache.put(key, value);
	}

	public Participant get(final String key) {
		try {
			logger.info("根据用户名称:{}从缓存中获取用户信息...", key );
			Participant participant = (Participant) cache.get(key, new Callable<Participant>() {
				@Override
				public Participant call() throws Exception {
					Participant selParticipant = participantService.getByParticipantName(key);
					if (selParticipant != null) {
						put(key.trim(), selParticipant);
						return selParticipant;
					}
					return null;
				}
			});
			return participant;
		} catch (ExecutionException e) {

		}
		return null;
	}

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
				if (participant != null) {
					String participantName = participant.getParticipantName();
					if (StringUtils.isNotBlank(participantName))
						logger.info("<<======用户：{}加入缓存...", participantName.trim());
					put(participantName.trim(), participant);
				}
			}
		}
		logger.info("<<==============参与人员信息加载完毕");
	}

}
