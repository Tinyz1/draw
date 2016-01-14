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
	 * 参与人员能够参与的次数
	 */
	private Cache<Integer, Integer> allPickCache = CacheBuilder.newBuilder().build();

	public synchronized void put(Integer id, Integer times) {
		allPickCache.put(id, times);
	}

	/**
	 * 使缓存中某个用户失效
	 * 
	 * @param key
	 *            用户ID
	 */
	public void subTimes(Integer id) {
		Integer times = get(id);
		if (--times == 0) {
			allPickCache.invalidate(id);
		} else {
			put(id, times);
		}
	}

	public void plusTimes(Integer id) {
		Integer times = get(id);
		put(id, ++times);
	}

	public List<Integer> getAll() {
		ConcurrentMap<Integer, Integer> participantTimes = allPickCache.asMap();
		List<Integer> participants = new ArrayList<Integer>();
		for (Map.Entry<Integer, Integer> participant : participantTimes.entrySet()) {
			int times = participant.getValue();
			if (times > 0) {
				for (int i = 0; i < times; i++) {
					participants.add(participant.getKey());
				}
			}
		}
		return participants;
	}

	public Integer get(final Integer id) {
		Integer times = 0;
		try {
			times = allPickCache.get(id, new Callable<Integer>() {
				@Override
				public Integer call() throws Exception {
					return 0;
				}
			});
		} catch (ExecutionException e) {
			return 0;
		}
		return times;

	}

	@Override
	public void afterPropertiesSet() throws Exception {
		List<Participant> participants = participantService.queryAllParticipant();
		if (participants != null && participants.size() > 0) {
			for (Participant participant : participants) {
				if (participant != null) {
					logger.info("<<--------------用户:{}含有{}次中奖机会...", participant.getParticipantName(), participant.getState());
					for (int i = 0, len = participant.getState(); i < len; i++) {
						plusTimes(participant.getParticipantId());
					}
				}
			}
		}
	}

}
