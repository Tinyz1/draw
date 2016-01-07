package com.asiainfo.draw.util;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;

/**
 * 默认的房间工厂实现类
 * 
 * @author yecl
 *
 */
public class DefaultRoomFactory extends AbstractRoomFactory {

	@Override
	protected Room createRoom(int id, String name, int len) {
		Preconditions.checkArgument(id > 0, "房间号不能小于零");
		Preconditions.checkArgument(StringUtils.isNotBlank(name), "房间名称不能为空！");
		Preconditions.checkArgument(len > 0, "房间大小不能小于零");
		return new DefaultRoom(id, name, len);
	}

}
