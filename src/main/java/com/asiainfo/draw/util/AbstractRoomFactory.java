package com.asiainfo.draw.util;

/**
 * 抽象房间工厂
 * 
 * @author yecl
 *
 */
public abstract class AbstractRoomFactory {

	protected AbstractRoomFactory() {
	}

	/**
	 * 创建房间
	 * 
	 * @param id
	 *            房间ID
	 * @param name
	 *            房间名称
	 * @param len
	 *            房间大小
	 * @return 新创建的房间
	 */
	protected abstract Room createRoom(int id, String name, int len);

}
