package com.asiainfo.draw.cache;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.asiainfo.draw.domain.DrawPrize;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * �н���¼����
 * 
 * @author yecl
 *
 */
@Component
@Scope("singleton")
public class HitPrizeCache {

	private final Logger logger = LoggerFactory.getLogger(ParticipantCache.class);

	private final Cache<Integer, Set<DrawPrize>> cache = CacheBuilder.newBuilder().build();

	/**
	 * ��¼�û��н���¼
	 * 
	 * @param participantNum
	 * @param linkId
	 */
	public synchronized void put(Integer participantId, DrawPrize drawPrize) {
		Set<DrawPrize> links = get(participantId);
		if (links == null) {
			links = new HashSet<DrawPrize>();
		}
		links.add(drawPrize);
		logger.info("�û���{}���н���{}", participantId, drawPrize);

		// ���»���
		cache.put(participantId, links);
	}

	public Set<DrawPrize> get(Integer participantId) {
		try {
			Set<DrawPrize> links = cache.get(participantId, new Callable<Set<DrawPrize>>() {
				@Override
				public Set<DrawPrize> call() throws Exception {
					return new HashSet<DrawPrize>();
				}
			});
			return links;
		} catch (ExecutionException e) {
			logger.error(e.toString());
		}
		return null;
	}
}
