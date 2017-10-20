package com.tx.model.constant;

import java.util.HashMap;
import java.util.Map;

public enum TimelineTypeEnum {
	DARK(1, 30), DARK_OP_RESULT(2, 10), LAST_WORDS(3, 30), DISCUSS(4, 30), VOTE(5, 30), VOTE_RESULT(6, 10), PK(7, 5), PK_DISCUSS_1(8, 30), PK_DISCUSS_2(9, 30), PK_VOTE(
			10, 30), PK_VOTE_RESULT(11, 10), WOLF_WIN(12, 5), FARMER_WIN(13, 5);

	/**
	 * 类型
	 */
	private final int type;
	/**
	 * 该环节时长s
	 */
	private final int times;

	private TimelineTypeEnum(int type, int times) {
		this.type = type;
		this.times = times;
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

	public int getTimes() {
		return times;
	}

}
