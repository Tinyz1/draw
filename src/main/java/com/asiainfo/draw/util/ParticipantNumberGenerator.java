package com.asiainfo.draw.util;

import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Preconditions;

/**
 * ������Ա���к�������
 * 
 * @author yecl
 *
 */
public final class ParticipantNumberGenerator {

	/**
	 * ���������Ѳ������û����к�
	 */
	private static Set<String> numbers = new HashSet<String>();

	/**
	 * ��ʼ���к�
	 */
	private static String currentNumber = "0000";

	/**
	 * ���кų���
	 */
	private static final int DEFAULT_SIZE = 4;

	/**
	 * ��߲�����ַ�
	 */
	private static final String LEFT_APPEND = "0";

	private ParticipantNumberGenerator() {
	}

	/**
	 * ������һ�����к�.�÷������̰߳�ȫ�ġ�
	 * 
	 * @return ��һ�����к�
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
	 * ���ص�ǰ���к�
	 * 
	 * @return ��ǰ���к�
	 */
	public static String current() {
		return currentNumber;
	}

	/**
	 * ���������кų��Ȳ���ʱ����߲��롣
	 * 
	 * @param number
	 *            ����������к�
	 * @return ���������к�
	 */
	private static String leftAppend(String number) {
		number = Preconditions.checkNotNull(number);
		for (int i = 0; i < DEFAULT_SIZE - number.length(); i++) {
			number = LEFT_APPEND + number;
		}
		return number;
	}
}
