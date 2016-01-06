package com.asiainfo.draw.util;

import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Preconditions;

/**
 * 参与人员序列号生成器
 * 
 * @author yecl
 *
 */
public final class ParticipantNumberGenerator {

	/**
	 * 保存所有已产生的用户序列号
	 */
	private static Set<String> numbers = new HashSet<String>();

	/**
	 * 初始序列号
	 */
	private static String currentNumber = "0000";

	/**
	 * 序列号长度
	 */
	private static final int DEFAULT_SIZE = 4;

	/**
	 * 左边补齐的字符
	 */
	private static final String LEFT_APPEND = "0";

	private ParticipantNumberGenerator() {
	}

	/**
	 * 产生下一个序列号.该方法是线程安全的。
	 * 
	 * @return 下一个序列号
	 */
	public static synchronized String next() {
		int number = Integer.parseInt(currentNumber);
		number++;
		String _number = String.valueOf(number);
		_number = leftAppend(_number);
		currentNumber = _number;
		numbers.add(currentNumber);
		return _number;
	}

	/**
	 * 返回当前序列号
	 * 
	 * @return 当前序列号
	 */
	public static String current() {
		return currentNumber;
	}

	/**
	 * 产生的序列号长度不足时，左边补齐。
	 * 
	 * @param number
	 *            待补齐的序列号
	 * @return 补齐后的序列号
	 */
	private static String leftAppend(String number) {
		number = Preconditions.checkNotNull(number);
		for (int i = 0; i < DEFAULT_SIZE - number.length(); i++) {
			number = LEFT_APPEND + number;
		}
		return number;
	}
}
