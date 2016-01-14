package com.asiainfo.draw.cache;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
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
public class CurrentLinkCache implements InitializingBean {

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
	 * ���ڿ�ʼʱ��
	 */
	public static final String CURRENT_START_DATE = KEY_PRE + "startDate";

	/**
	 * ���ڽ���ʱ��
	 */
	public static final String CURRENT_FINISH_DATE = KEY_PRE + "finishDate";

	/**
	 * ���ڲ�����
	 */
	public static final String CURRENT_PARTICIPANTS = KEY_PRE + "participants";

	/**
	 * ���ڽ�Ʒ
	 */
	public static final String CURRENT_PRIZES = KEY_PRE + "prizes";

	/**
	 * ��ǰ�����н���¼
	 */
	public static final String CURRENT_HIT = KEY_PRE + "hit";

	/**
	 * ����ҡ����¼
	 */
	public static final String CURRENT_SHAKE = KEY_PRE + "shake";

	/**
	 * ���ڲ�������
	 */
	public static final String CURRENT_PICK_NUM = KEY_PRE + "pickNum";

	/**
	 * ���ڽ�����
	 */
	public static final String CURRENT_ENTER_NUMBER = KEY_PRE + "enterNumber";

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
	 * ��ʼ����һ������
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		linkService.initNextLink();
	}

	/**
	 * ��յ�ǰ���ڻ���
	 */
	public void clear() {
		cache = CacheBuilder.newBuilder().build();
	}

}
