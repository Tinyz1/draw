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

@Component
public class ParticipantCache implements InitializingBean {

	private final Logger logger = LoggerFactory.getLogger(ParticipantCache.class);

	private final Cache<String, Object> cache = CacheBuilder.newBuilder().build();

	public synchronized void put(Integer key, Participant value) {
		logger.info("写入缓存，key: {}, value: {}", key, value);
		cache.put(String.valueOf(key), value);
	}

	public Participant get(final Integer key) {
		try {
			Participant participant = (Participant) cache.get(String.valueOf(key), new Callable<Participant>() {
				@Override
				public Participant call() throws Exception {
					Participant selParticipant = participantService.getByParticipantNum(key);
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

	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("<<==============开始初始化参与人员信息");
		logger.info("<<==============查询数据库中信息");
		List<Participant> participants = participantService.queryAllParticipant();

		if (participants != null && participants.size() > 0) {
			for (Participant participant : participants) {
				put(participant.getParticipantNum(), participant);
			}
		}
		logger.info("<<==============参与人员信息加载完毕");
	}

}
