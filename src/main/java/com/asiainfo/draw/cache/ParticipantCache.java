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

	/**
	 * 保存ID键值对
	 */
	private Cache<Integer, Participant> idCache = CacheBuilder.newBuilder().build();

	/**
	 * 保存名称键值对
	 */
	private Cache<String, Participant> nameCache = CacheBuilder.newBuilder().build();

	@Autowired
	private ParticipantService participantService;

	public synchronized void put(Integer id, Participant value) {
		logger.info("写入缓存，key: {}, value: {}", id, value);
		idCache.put(id, value);
	}

	public synchronized void put(String name, Participant value) {
		logger.info("写入缓存，key: {}, value: {}", name, value);
		nameCache.put(name, value);
	}

	public Participant get(final Integer id) {
		try {
			logger.info("根据用户ID:{}从缓存中获取用户信息...", id);
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
			logger.info("根据用户名称:{}从缓存中获取用户信息...", name);
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

	@Override
	public void afterPropertiesSet() throws Exception {
		load();
	}

	/**
	 * 把用户信息加载到缓存中
	 */
	public void load() {
		logger.info("<<---------开始加载用户信息到缓存中....");
		List<Participant> participants = participantService.queryAllParticipant();
		if (participants != null && participants.size() > 0) {
			for (Participant participant : participants) {
				if (participant != null) {
					logger.info("<<--------------用户：{}加入缓存中...", participant);
					put(participant.getParticipantId(), participant);
					put(participant.getParticipantName(), participant);
				}
			}
		}
		logger.info("<<---------加载用户信息结束....");
	}

	/**
	 * 重新加载用户信息到缓存中
	 */
	public void reload() {
		idCache = CacheBuilder.newBuilder().build();
		nameCache = CacheBuilder.newBuilder().build();
		load();
	}

}
