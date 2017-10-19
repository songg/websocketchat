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
	
	/**
	 * 玩家编号
	 */
	List<Integer> playerIndex; 
	
	/**
	 * 房间对应的剧情时间轴
	 */
	List<TimeLineVO> timelines;
	
	
	public Room() {
		playerIndex = new ArrayList<>();
		playerIndex.add(1);
		playerIndex.add(2);
		playerIndex.add(3);
		playerIndex.add(4);
		playerIndex.add(5);
		playerIndex.add(6);
	}

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

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
	
	public List<Integer> getPlayerIndex() {
		return playerIndex;
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

	public List<TimeLineVO> getTimelines() {
		return timelines;
	}

	public void setTimelines(List<TimeLineVO> timelines) {
		this.timelines = timelines;
	}
}
