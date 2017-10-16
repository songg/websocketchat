package com.tx.model.constant;

import java.util.HashMap;
import java.util.Map;

public enum PlayerOPEnum {
	KILL(1), IDENTIFY(2), GUARD(3), VOTE(4);

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
