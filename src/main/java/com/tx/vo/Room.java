package com.tx.vo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

public class Room {
	/**
	 * 房间id
	 */
	private String roomId;
	
	/**
	 * 房主index
	 */
	private int holderIndex;
	
	/**
	 * 玩家列表
	 */
	private List<UserVO> players;
	/**
	 * 房间状态: 0,准备中. 1,游戏中
	 */
	private int status;
	
	/**
	 * 监听用秘钥。防止别人恶意监听
	 */
	private String privateKey;

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public List<UserVO> getPlayers() {
		return players;
	}

	public void setPlayers(List<UserVO> players) {
		this.players = players;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public int getHolderIndex() {
		return holderIndex;
	}

	public void setHolderIndex(int holderIndex) {
		this.holderIndex = holderIndex;
	}

	public static void main(String[] args) {
		Room r = new Room();
		r.setRoomId("1");
		r.setStatus(0);
		List<UserVO> players = new ArrayList<>();
		UserVO vo1 = new UserVO();
		vo1.setIndex(1);
		vo1.setLevel(10);
		vo1.setName("player1");
		players.add(vo1);
		
		UserVO vo2 = new UserVO();
		vo2.setIndex(2);
		vo2.setLevel(10);
		vo2.setName("player2");
		players.add(vo2);
		
		r.setPlayers(players);
		
		for (UserVO userVO : r.getPlayers()) {
			System.out.println(userVO.getIndex());
		}
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
	
	public UserVO getPlayer(int playerIndex) {
		if(CollectionUtils.isNotEmpty(players)) {
			for(UserVO userVO : players) {
				if(userVO.getIndex() == playerIndex) {
					return userVO;
				}
			}
		}
		return null;
	}
}
