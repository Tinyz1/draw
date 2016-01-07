package com.asiainfo.draw.util;

import com.asiainfo.draw.domain.Participant;

public class DefaultRoom extends Room {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3723402576812329210L;

	public DefaultRoom() {
		super();
	}

	public DefaultRoom(int id, String name, int len) {
		super(id, name, len);
	}

	@Override
	public void add(Participant participant) {

	}

	@Override
	public void remove(Participant participant) {

	}

	@Override
	public void clear() {

	}

	@Override
	public boolean exist(Participant participant) {
		return false;
	}

}
