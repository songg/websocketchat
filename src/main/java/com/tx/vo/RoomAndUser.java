package com.tx.vo;

public class RoomAndUser {
	private String roomId;
	
	/**
	 * 加入玩家
	 */
	private UserVO user;
	
	/**
	 * 房间秘钥
	 */
	private String roomPrivateKey;
	
	
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
	public String getRoomPrivateKey() {
		return roomPrivateKey;
	}
	public void setRoomPrivateKey(String roomPrivateKey) {
		this.roomPrivateKey = roomPrivateKey;
	}
}
