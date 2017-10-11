package com.tx.vo;

public class RoomAndUser {
	private String roomId;
	
	/**
	 * 加入玩家
	 */
	private UserVO user;
	
	
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public UserVO getUser() {
		return user;
	}
	public void setUser(UserVO user) {
		this.user = user;
	}
}
