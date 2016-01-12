package com.asiainfo.draw.cache;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
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
@Scope("singleton")
public class CurrentLinkCache implements InitializingBean {

	private static final String KEY_PRE = "com.asiainfo.draw.cache.CurrentLinkCache.";
	/**
	 * 当前环节
	 */
	public static final String CURRENT_LINK = KEY_PRE + "link";

	/**
	 * 环节奖品池
	 */
	public static final String CURRENT_POOLS = KEY_PRE + "pools";

	/**
	 * 当前环节是否开始
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
	 * 环节开始时间
	 */
	public static final String CURRENT_START_DATE = KEY_PRE + "startDate";

	/**
	 * 环节结束时间
	 */
	public static final String CURRENT_FINISH_DATE = KEY_PRE + "finishDate";

	/**
	 * 环节参与人数
	 */
	public static final String CURRENT_PARTICIPANTS = KEY_PRE + "participants";

	/**
	 * 环节奖品
	 */
	public static final String CURRENT_PRIZES = KEY_PRE + "prizes";

	/**
	 * 当前环节中奖记录
	 */
	public static final String CURRENT_HIT = KEY_PRE + "hit";
	
	/**
	 * 当前环节摇奖记录
	 */
	public static final String CURRENT_SHAKE = KEY_PRE + "shake";
	

	private final Logger logger = LoggerFactory.getLogger(ParticipantCache.class);

	@Autowired
	private LinkService linkService;

	private Cache<String, Object> cache = CacheBuilder.newBuilder().build();

	public synchronized void put(String key, Object value) {
		logger.debug("push-->key:{},value:{}", key, value);
		cache.put(key, value);
	}

	public Object get(String key) {
		try {
			return cache.get(key, new Callable<Object>() {
				@Override
				public Object call() throws Exception {
					return null;
				}
			});
		} catch (ExecutionException e) {

		}
		return null;
	}

	/**
	 * 初始化第一个环节
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		linkService.initNextLink();
	}

	/**
	 * 清空当前环节缓存
	 */
	public void clear() {
		cache = CacheBuilder.newBuilder().build();
	}

}
