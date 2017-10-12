package com.tx.vo;

public class TimeLineVO {
	/**
	 * 1:天亮了，0:天黑请闭眼
	 */
	private int day;
	
	/**
	 * 被杀者编号
	 */
	private int deadIndex;
	
	/**
	 * 1:游戏继续。 2:狼人胜利，3:村民胜利
	 */
	private int type;

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
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
	
	
}
