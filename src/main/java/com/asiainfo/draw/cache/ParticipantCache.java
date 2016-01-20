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
 * 参与人员缓存
 * 
 * @author yecl
 *
 */
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
		idCache.put(id, value);
	}

	public synchronized void put(String name, Participant value) {
		nameCache.put(name, value);
	}

	public Participant reload(final Integer id) {
		idCache.invalidate(id);
		return get(id);
	}

	public Participant reload(final String name) {
		nameCache.invalidate(name);
		return get(name);
	}

	public Participant get(final Integer id) {
		try {
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
		List<Participant> participants = participantService.queryAllParticipant();
		if (participants != null && participants.size() > 0) {
			for (Participant participant : participants) {
				if (participant != null) {
					put(participant.getParticipantId(), participant);
					put(participant.getParticipantName(), participant);
				}
			}
		}
	}

	/**
	 * 重新加载用户信息到缓存中
	 */
	public void reload() {
		idCache.invalidateAll();
		nameCache.invalidateAll();
		load();
	}

}
