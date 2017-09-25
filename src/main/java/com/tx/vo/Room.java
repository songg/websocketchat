package com.tx.vo;

import java.util.List;

public class Room {
	/**
	 * 房间id
	 */
	private String roomId;
	/**
	 * 玩家列表
	 */
	private List<String> players;
	/**
	 * 房间状态: 0,准备中. 1,游戏中
	 */
	private int status;

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public List<String> getPlayers() {
		return players;
	}

	public void setPlayers(List<String> players) {
		this.players = players;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
