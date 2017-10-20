package com.tx.model.constant;

import java.util.HashMap;
import java.util.Map;

public enum TimelineTypeEnum {
	DARK(1), DARK_OP_RESULT(2), LAST_WORDS(3), DISCUSS(4), VOTE(5), VOTE_RESULT(6), PK(7), PK_DISCUSS(8), PK_VOTE(
			9), PK_VOTE_RESULT(10), WOLF_WIN(11), FARMER_WIN(12);

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
