package com.tx.tools;

import com.tx.vo.TimeLineVO;

public class TimeLineTrigger {
	public static TimeLineVO nextTimeLine(TimeLineVO currentTimeLine) {
		TimeLineVO nextTimeLineVO = new TimeLineVO();
		nextTimeLineVO.setDay(!currentTimeLine.getDay());
		if (!currentTimeLine.getDay()) {
			nextTimeLineVO.setDateCount(currentTimeLine.getDateCount() + 1);
		}
		nextTimeLineVO.setDeadIndex(0);
		nextTimeLineVO.setType(1);
		nextTimeLineVO.setVoteOps(null);
		return nextTimeLineVO;
	} 
}
