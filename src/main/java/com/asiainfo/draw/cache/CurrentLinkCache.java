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
 * ���浱ǰ���ڵ�������Ϣ��
 * 
 * @author yecl
 *
 */
@Component
public class CurrentLinkCache {

	private static final String KEY_PRE = "com.asiainfo.draw.cache.CurrentLinkCache.";
	/**
	 * ��ǰ����
	 */
	public static final String CURRENT_LINK = KEY_PRE + "link";

	/**
	 * ���ڽ�Ʒ��
	 */
	public static final String CURRENT_POOL = KEY_PRE + "pool";

	/**
	 * ����״̬
	 */
	public static final String CURRENT_STATE = KEY_PRE + "state";

	/**
	 * ����״̬
	 * 
	 * @author yecl
	 *
	 */
	public enum LinkState {
		INIT, // �����ѳ�ʼ��
		RUN, // ���ڽ�����
		FINISH // �����ѽ���
	}

	/**
	 * ���ڲ�����
	 */
	public static final String CURRENT_PARTICIPANTS = KEY_PRE + "participants";

	/**
	 * ���ڽ�Ʒ
	 */
	public static final String CURRENT_PRIZES = KEY_PRE + "prizes";


	private final Logger logger = LoggerFactory.getLogger(ParticipantCache.class);

	@Autowired
	private LinkService linkService;

	private Cache<String, Object> cache = CacheBuilder.newBuilder().build();

	public synchronized void put(String key, Object value) {
		logger.info("���뻺��->key:{},value:{}", key, value);
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
		logger.info("��ȡ����->key:{},value:{}", key, value);
		return value;
	}

	/**
	 * ��յ�ǰ���ڻ���
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
