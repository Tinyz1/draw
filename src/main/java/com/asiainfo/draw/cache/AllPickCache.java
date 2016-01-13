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

/**
 * 缓存所有能够进行抽人环节的人员
 * 
 * @author yecl
 *
 */
@Component
public class AllPickCache implements InitializingBean {

	private final Logger logger = LoggerFactory.getLogger(AllPickCache.class);

	@Autowired
	private ParticipantService participantService;

	/**
	 * 保存所有能够参与抽人的人员
	 */
	private Cache<Integer, Participant> allPickCache = CacheBuilder.newBuilder().build();

	public synchronized void put(Integer id, Participant value) {
		logger.info("把用户：{}加入可抽人员列表缓存中...", value);
		allPickCache.put(id, value);
	}

	/**
	 * 使缓存中某个用户失效
	 * 
	 * @param key
	 *            用户ID
	 */
	public void invalidate(Integer key) {
		allPickCache.invalidate(key);
	}

	public List<Participant> getAll() {
		ConcurrentMap<Integer, Participant> maps = allPickCache.asMap();
		List<Participant> participants = new ArrayList<Participant>();
		for (Map.Entry<Integer, Participant> map : maps.entrySet()) {
			participants.add(map.getValue());
		}
		return participants;
	}

	public Participant get(final Integer id) {
		try {
			Participant participant = (Participant) allPickCache.get(id, new Callable<Participant>() {
				@Override
				public Participant call() throws Exception {
					return null;
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
		List<Participant> participants = participantService.queryAllParticipant();
		if (participants != null && participants.size() > 0) {
			for (Participant participant : participants) {
				if (participant != null) {
					logger.info("<<--------------用户：{}加入缓存中...", participant);
					put(participant.getParticipantId(), participant);
				}
			}
		}
	}

}
