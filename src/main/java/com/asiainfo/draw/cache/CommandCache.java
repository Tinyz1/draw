package com.asiainfo.draw.cache;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.asiainfo.draw.util.Command;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * 指令缓存
 * 
 * @author yecl
 *
 */
@Component
public class CommandCache {

	private final Logger logger = LoggerFactory.getLogger(CommandCache.class);

	private static final String KEY_PRE = "com.asiainfo.draw.cache.CommandCache.";
	/**
	 * 当前环节
	 */
	public static final String CURRENT_COMMAND = KEY_PRE + "command";

	private final Cache<String, Command> cache = CacheBuilder.newBuilder().build();

	public synchronized void put(String key, Command value) {
		logger.info(">>放入指令 - key:{},value:{}", key, value);
		cache.put(key, value);
	}

	public void invalidate() {
		cache.invalidate(CURRENT_COMMAND);
	}

	public Command get(String key) {
		try {
			return cache.get(key, new Callable<Command>() {
				@Override
				public Command call() throws Exception {
					return new Command();
				}
			});
		} catch (ExecutionException e) {
			logger.error(e.toString());
		}
		return null;
	}

}
