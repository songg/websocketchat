package com.tx.vo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.tx.model.constant.RoleEnum;
import com.tx.model.constant.TimelineTypeEnum;
import com.tx.tools.LrsJudger;
import com.tx.tools.TimeLineTrigger;

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
	 * 座位编号，预先生成且固定
	 */
	List<Integer> playerIndex;

	/**
	 * 存活的座位编号,每死一个从中就从这里剔除
	 */
	List<Integer> liveIndex;

	/**
	 * 自由发言阶段能发言的座位编号,发言后从里面去掉,每晚基于liveIndex重置
	 */
	List<Integer> canTalkIndex;

	/**
	 * 房间对应的剧情时间轴
	 */
	List<TimeLineVO> timelines;

	/**
	 * 人类死亡数量，用来判断胜负用
	 */
	private int humanityDeadNum = 0;

	/**
	 * 狼人死亡数量，判断胜负用
	 */
	private int wolfDeadNum = 0;

	public Room() {
		playerIndex = new ArrayList<>();
		playerIndex.add(1);
		playerIndex.add(2);
		playerIndex.add(3);
		playerIndex.add(4);
		playerIndex.add(5);
		playerIndex.add(6);

		canTalkIndex.addAll(playerIndex);
		liveIndex.addAll(playerIndex);
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
		if (CollectionUtils.isNotEmpty(players)) {
			for (UserVO userVO : players) {
				if (userVO.getIndex() == playerIndex) {
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

	public int getHumanityDeadNum() {
		return humanityDeadNum;
	}

	public void setHumanityDeadNum(int humanityDeadNum) {
		this.humanityDeadNum = humanityDeadNum;
	}

	public int getWolfDeadNum() {
		return wolfDeadNum;
	}

	public void setWolfDeadNum(int wolfDeadNum) {
		this.wolfDeadNum = wolfDeadNum;
	}

	public void addDeadNum(int deadIndex) {
		if (this.getPlayer(deadIndex).getRole() == RoleEnum.WOLF.getRole()) {
			this.setWolfDeadNum(this.getWolfDeadNum() + 1);
		} else {
			this.setHumanityDeadNum(this.getHumanityDeadNum() + 1);
		}
	}

	public void reduceDeadNum(int deadIndex) {
		if (this.getPlayer(deadIndex).getRole() == RoleEnum.WOLF.getRole()) {
			this.setWolfDeadNum(this.getWolfDeadNum() - 1);
		} else {
			this.setHumanityDeadNum(this.getHumanityDeadNum() - 1);
		}
	}

	public List<Integer> getLiveIndex() {
		return liveIndex;
	}

	public void setLiveIndex(List<Integer> liveIndex) {
		this.liveIndex = liveIndex;
	}

	public List<Integer> getCanTalkIndex() {
		return canTalkIndex;
	}

	public void setCanTalkIndex(List<Integer> canTalkIndex) {
		this.canTalkIndex = canTalkIndex;
	}

	public void startJudger(SimpMessagingTemplate simpMessagingTemplate) {
		LrsJudger j = new LrsJudger(this, simpMessagingTemplate);
		j.start();

	

	}
}
