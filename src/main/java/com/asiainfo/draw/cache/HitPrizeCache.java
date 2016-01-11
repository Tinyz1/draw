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
 * 中奖记录缓存
 * 
 * @author yecl
 *
 */
@Component
@Scope("singleton")
public class HitPrizeCache {

	private final Logger logger = LoggerFactory.getLogger(ParticipantCache.class);

	private final Cache<String, Set<DrawPrize>> cache = CacheBuilder.newBuilder().build();

	/**
	 * 记录用户中奖记录
	 * 
	 * @param participantNum
	 * @param linkId
	 */
	public synchronized void put(String participantName, DrawPrize drawPrize) {
		Set<DrawPrize> links = get(participantName);
		if (links == null) {
			links = new HashSet<DrawPrize>();
		}
		links.add(drawPrize);
		logger.info("写入缓存，participantName: {}, linkId: {}", participantName, drawPrize);
		cache.put(participantName, links);
	}

	public Set<DrawPrize> get(String participantName) {
		try {
			Set<DrawPrize> links = cache.get(participantName, new Callable<Set<DrawPrize>>() {
				@Override
				public Set<DrawPrize> call() throws Exception {
					return new HashSet<DrawPrize>();
				}
			});
			return links;
		} catch (ExecutionException e) {
		}

		return null;
	}
}
