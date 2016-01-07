package com.asiainfo.draw.util;

import java.io.Serializable;

import com.asiainfo.draw.domain.Participant;

/**
 * 抽象抽奖房间概念
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
	 * 房间ID
	 */
	private int id;

	/**
	 * 房间名称
	 */
	private String name;

	/**
	 * 房间可容纳的人数
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
	 * 房间加人
	 * 
	 * @param participant
	 */
	public abstract void add(Participant participant);

	/**
	 * 房间踢人
	 * 
	 * @param participant
	 */
	public abstract void remove(Participant participant);

	/**
	 * 清空房间的人员
	 */
	public abstract void clear();

	/**
	 * 判断某个人是否在房间里面
	 * 
	 * @param participant
	 *            待判断的人员
	 * @return 在房间里面时，返回true.否则返回false
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
