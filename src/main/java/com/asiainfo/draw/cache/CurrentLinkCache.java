package com.asiainfo.draw.cache;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * 缓存当前环节的所有信息。
 * 
 * @author yecl
 *
 */
public class CurrentLinkCache {

	private static Cache<String, Object> cache = CacheBuilder.newBuilder().build();

	public synchronized static void put(String key, Object value) {
		cache.put(key, value);
	}

	public static Object get(String key) {
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

}
