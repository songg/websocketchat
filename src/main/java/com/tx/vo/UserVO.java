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
	
	/**
	 * 状态：0. 空闲中, 1.游戏中
	 */
	private int status;
	
	/**
	 * 所分配角色
	 */
	private int role;
	
	/**
	 * 监听用秘钥，避免别人恶意监听私人信息
	 */
	private String privateKey;
	
	
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
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}
	public String getPrivateKey() {
		return privateKey;
	}
	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
}
