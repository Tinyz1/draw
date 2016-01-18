package com.asiainfo.draw.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.asiainfo.draw.domain.DrawPrize;
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

	private final Cache<Integer, Map<String, DrawPrize>> cache = CacheBuilder.newBuilder().build();

	/**
	 * 记录环节用户中奖记录
	 * 
	 * @param linkId
	 * @param hitPrize
	 */
	public synchronized void put(Integer linkId, Map<String, DrawPrize> hitPrize) {
		// 更新缓存
		cache.put(linkId, hitPrize);
	}

	public Map<String, DrawPrize> get(Integer linkId) {
		try {
			Map<String, DrawPrize> hitPrize = cache.get(linkId, new Callable<Map<String, DrawPrize>>() {
				@Override
				public Map<String, DrawPrize> call() throws Exception {
					return new HashMap<String, DrawPrize>();
				}
			});
			return hitPrize;
		} catch (ExecutionException e) {
			logger.error(e.toString());
		}
		return null;
	}
}
