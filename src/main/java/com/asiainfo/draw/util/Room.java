package com.asiainfo.draw.util;

import java.io.Serializable;

import com.asiainfo.draw.domain.Participant;

/**
 * ����齱�������
 * 
 * @author yecl
 *
 */
public abstract class Room implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ����ID
	 */
	private int id;

	/**
	 * ��������
	 */
	private String name;

	/**
	 * ��������ɵ�����
	 */
	private int len;

	public Room() {
		super();
	}

	public Room(int id, String name, int len) {
		super();
		this.id = id;
		this.name = name;
		this.len = len;
	}

	/**
	 * �������
	 * 
	 * @param participant
	 */
	public abstract void add(Participant participant);

	/**
	 * ��������
	 * 
	 * @param participant
	 */
	public abstract void remove(Participant participant);

	/**
	 * ��շ������Ա
	 */
	public abstract void clear();

	/**
	 * �ж�ĳ�����Ƿ��ڷ�������
	 * 
	 * @param participant
	 *            ���жϵ���Ա
	 * @return �ڷ�������ʱ������true.���򷵻�false
	 */
	public abstract boolean exist(Participant participant);

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLen() {
		return len;
	}

	public void setLen(int len) {
		this.len = len;
	}

}
