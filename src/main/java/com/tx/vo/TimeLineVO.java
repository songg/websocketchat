package com.tx.vo;

import java.util.List;
import java.util.Map;

public class TimeLineVO {
	/**
	 * true:天亮了，false:天黑请闭眼
	 */
	private boolean day;
	
	/**
	 * 被杀者编号
	 */
	private int deadIndex;
	
	/**
	 * 1:游戏继续。 2:狼人胜利，3:村民胜利, 4:平票PK
	 */
	private int type;
	
	/**
	 * 投票行为
	 */
	private Map<Integer, Integer> voteOps;
	
	/**
	 * 天数
	 */
	private int dateCount;
	
	/**
	 * 平票待PK玩家
	 */
	private List<Integer> pk;

	public boolean getDay() {
		return day;
	}

	public void setDay(boolean day) {
		this.day = day;
	}

	public int getDeadIndex() {
		return deadIndex;
	}

	public void setDeadIndex(int deadIndex) {
		this.deadIndex = deadIndex;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Map<Integer, Integer> getVoteOps() {
		return voteOps;
	}

	public void setVoteOps(Map<Integer, Integer> voteOps) {
		this.voteOps = voteOps;
	}

	public int getDateCount() {
		return dateCount;
	}

	public void setDateCount(int dateCount) {
		this.dateCount = dateCount;
	}

	public List<Integer> getPk() {
		return pk;
	}

	public void setPk(List<Integer> pk) {
		this.pk = pk;
	}
	
}
