package com.asiainfo.draw.util;

/**
 * ���󷿼乤��
 * 
 * @author yecl
 *
 */
public abstract class AbstractRoomFactory {

	protected AbstractRoomFactory() {
	}

	/**
	 * ��������
	 * 
	 * @param id
	 *            ����ID
	 * @param name
	 *            ��������
	 * @param len
	 *            �����С
	 * @return �´����ķ���
	 */
	protected abstract Room createRoom(int id, String name, int len);

}
