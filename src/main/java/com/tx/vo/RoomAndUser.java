package com.tx.vo;

public class RoomAndUser {
	private String roomId;
	
	/**
	 * 加入玩家
	 */
	private UserVO user;
	
	/**
	 * 房主下标
	 */
	private int holderIndex;
	
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
	public int getHolderIndex() {
		return holderIndex;
	}
	public void setHolderIndex(int holderIndex) {
		this.holderIndex = holderIndex;
	}
	
	
}
