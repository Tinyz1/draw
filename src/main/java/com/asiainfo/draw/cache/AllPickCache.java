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
 * ���������ܹ����г��˻��ڵ���Ա
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
	 * ���������ܹ�������˵���Ա
	 */
	private Cache<Integer, Participant> allPickCache = CacheBuilder.newBuilder().build();

	public synchronized void put(Integer id, Participant value) {
		logger.info("���û���{}����ɳ���Ա�б�����...", value);
		allPickCache.put(id, value);
	}

	/**
	 * ʹ������ĳ���û�ʧЧ
	 * 
	 * @param key
	 *            �û�ID
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
					logger.info("<<--------------�û���{}���뻺����...", participant);
					put(participant.getParticipantId(), participant);
				}
			}
		}
	}

}
