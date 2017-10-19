package com.tx.model.constant;

import java.util.HashMap;
import java.util.Map;

public enum TimelineTypeEnum {
	DARK(1), DARK_OP_RESULT(2), LAST_WORDS(3), DISCUSS(4), VOTE(5), VOTE_RESULT(6), VOTE_PK(7), VOTE_PK_RESULT(8), WOLF_WIN(9), FARMER_WIN(10);

	private final int type;

	private TimelineTypeEnum(int type) {
		this.type = type;
	}
	
	private static Map<Integer, TimelineTypeEnum> cache = new HashMap<Integer, TimelineTypeEnum>();
	static {
		for (TimelineTypeEnum timeLineType : TimelineTypeEnum.values()) {
			cache.put(timeLineType.getType(), timeLineType);
		}
	}
	
	public static TimelineTypeEnum from(int type) {
		return cache.get(type);
	}

	public int getType() {
		return type;
	}
	
	
}
