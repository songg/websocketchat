package com.tx.model.constant;

public enum RoleEnum {
	WOLF(1), FARMER(2), GUARD(3), SEER(4);

	private final int role;

	private RoleEnum(int role) {
		this.role = role;
	}

	public int getRole() {
		return role;
	}
	
}
