package com.tx.model.constant;

import java.util.HashMap;
import java.util.Map;

public enum PlayerOPEnum {
	START(1), KILL(2), IDENTIFY(3), GUARD(4), VOTE(5);

	private final int opType;

	private PlayerOPEnum(int opType) {
		this.opType = opType;
	}

	public int getOpType() {
		return opType;
	}
	
	
	private static Map<Integer, PlayerOPEnum> cache = new HashMap<Integer, PlayerOPEnum>();
	static {
		for (PlayerOPEnum playerOP : PlayerOPEnum.values()) {
			cache.put(playerOP.getOpType(), playerOP);
		}
	}
	
	public static PlayerOPEnum from(int opType) {
		return cache.get(opType);
	}
}
