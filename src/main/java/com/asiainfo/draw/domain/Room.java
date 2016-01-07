package com.asiainfo.draw.domain;

import java.util.Random;

public class Room {
	private Integer roomId;

	private String roomName;

	private String roomDesc;

	private String instruction;

	private Integer len;

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName == null ? null : roomName.trim();
	}

	public String getRoomDesc() {
		return roomDesc;
	}

	public void setRoomDesc(String roomDesc) {
		this.roomDesc = roomDesc == null ? null : roomDesc.trim();
	}

	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction == null ? null : instruction.trim();
	}

	public Integer getLen() {
		return len;
	}

	public void setLen(Integer len) {
		this.len = len;
	}

	/**
	 * ���ɷ���ָ��.ָ��Ϊ6λ���ȵ�����.
	 * 
	 * @return ����ָ��
	 */
	public static String generateInstruction() {
		Random random = new Random();
		String instruction = String.valueOf(random.nextInt(9) + 1); // 1--9(����9)
		for (int i = 0; i < 5; i++) {
			instruction += random.nextInt(10); // 0-10(������10);
		}
		return instruction;
	}
}