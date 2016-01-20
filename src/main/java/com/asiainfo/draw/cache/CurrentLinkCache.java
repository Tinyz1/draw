package com.asiainfo.draw.cache;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.asiainfo.draw.service.LinkService;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * 缓存当前环节的所有信息。
 * 
 * @author yecl
 *
 */
@Component
public class CurrentLinkCache {

	private static final String KEY_PRE = "com.asiainfo.draw.cache.CurrentLinkCache.";
	/**
	 * 当前环节
	 */
	public static final String CURRENT_LINK = KEY_PRE + "link";

	/**
	 * 环节奖品池
	 */
	public static final String CURRENT_POOL = KEY_PRE + "pool";

	/**
	 * 环节状态
	 */
	public static final String CURRENT_STATE = KEY_PRE + "state";

	/**
	 * 环节状态
	 * 
	 * @author yecl
	 *
	 */
	public enum LinkState {
		INIT, // 环节已初始化
		RUN, // 环节进行中
		FINISH // 环节已结束
	}

	/**
	 * 环节参与人
	 */
	public static final String CURRENT_PARTICIPANTS = KEY_PRE + "participants";

	/**
	 * 环节奖品
	 */
	public static final String CURRENT_PRIZES = KEY_PRE + "prizes";


	private final Logger logger = LoggerFactory.getLogger(ParticipantCache.class);

	@Autowired
	private LinkService linkService;

	private Cache<String, Object> cache = CacheBuilder.newBuilder().build();

	public synchronized void put(String key, Object value) {
		logger.info("加入缓存->key:{},value:{}", key, value);
		cache.put(key, value);
	}

	public Object get(String key) {
		Object value = null;
		try {
			value = cache.get(key, new Callable<Object>() {
				@Override
				public Object call() throws Exception {
					return "";
				}
			});
		} catch (ExecutionException e) {
			logger.error(e.toString());
		}
		logger.info("获取缓存->key:{},value:{}", key, value);
		return value;
	}

	/**
	 * 清空当前环节缓存
	 */
	public void invalidateAll() {
		cache.invalidateAll();
	}

	/**
	 * 
	 * @param key
	 */
	public void invalidata(String key) {
		cache.invalidate(key);
	}

}
