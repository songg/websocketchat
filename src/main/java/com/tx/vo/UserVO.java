package com.tx.vo;

public class UserVO {
	/**
	 * 房间内编号
	 */
	private int index;
	/**
	 * 玩家名
	 */
	private String name;
	
	/**
	 * 级别
	 */
	private int level;
	
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getName() {
		return name;
	} 
	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
}
