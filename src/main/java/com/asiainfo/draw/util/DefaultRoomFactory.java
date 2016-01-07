package com.asiainfo.draw.util;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;

/**
 * Ĭ�ϵķ��乤��ʵ����
 * 
 * @author yecl
 *
 */
public class DefaultRoomFactory extends AbstractRoomFactory {

	@Override
	protected Room createRoom(int id, String name, int len) {
		Preconditions.checkArgument(id > 0, "����Ų���С����");
		Preconditions.checkArgument(StringUtils.isNotBlank(name), "�������Ʋ���Ϊ�գ�");
		Preconditions.checkArgument(len > 0, "�����С����С����");
		return new DefaultRoom(id, name, len);
	}

}
