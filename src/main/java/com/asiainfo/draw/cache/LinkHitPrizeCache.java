package com.asiainfo.draw.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * 环节中奖记录
 * 
 * @author yecl
 *
 */
@Component
public class LinkHitPrizeCache {

	private final Logger logger = LoggerFactory.getLogger(ParticipantCache.class);

	private final Cache<Integer, Map<String, String>> cache = CacheBuilder.newBuilder().build();

	/**
	 * 记录环节用户中奖记录
	 * 
	 * @param linkId
	 * @param hitPrize
	 */
	public synchronized void put(Integer linkId, Map<String, String> hitPrize) {
		// 更新缓存
		cache.put(linkId, hitPrize);
	}

	public Map<String, String> get(Integer linkId) {
		try {
			Map<String, String> hitPrize = cache.get(linkId, new Callable<Map<String, String>>() {
				@Override
				public Map<String, String> call() throws Exception {
					return new HashMap<String, String>();
				}
			});
			return hitPrize;
		} catch (ExecutionException e) {
			logger.error(e.toString());
		}
		return null;
	}
}
