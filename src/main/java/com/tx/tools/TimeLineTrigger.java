package com.tx.tools;

import com.tx.model.constant.Constants;
import com.tx.model.constant.TimelineTypeEnum;
import com.tx.vo.TimeLineVO;

public class TimeLineTrigger {
	public static TimeLineVO nextTimeLine(TimeLineVO currentTimeLine) {
		TimeLineVO nextTimeLineVO = new TimeLineVO();
		switch (TimelineTypeEnum.from(currentTimeLine.getType())) {
		case DARK:
			// 当前阶段为天黑阶段，进入今晚用户行为结果广播阶段
			nextTimeLineVO.setType(TimelineTypeEnum.DARK_OP_RESULT.getType());
			nextTimeLineVO.setDay(true);
			nextTimeLineVO.setDateCount(currentTimeLine.getDateCount() + 1);
			nextTimeLineVO.setTimes(Constants.DARK_OP_RESULT_TIME);
			break;
		case DARK_OP_RESULT:
			// 当前阶段为广播昨晚用户行为结果阶段，进入遗言或者自由讨论时间
			if (currentTimeLine.getDeadIndex() > 0) {
				nextTimeLineVO.setType(TimelineTypeEnum.LAST_WORDS.getType());
				nextTimeLineVO.setDay(true);
				nextTimeLineVO.setTalkIndex(currentTimeLine.getDeadIndex());
				nextTimeLineVO.setDateCount(currentTimeLine.getDateCount());
				nextTimeLineVO.setTimes(Constants.LAST_WORDS_TIME);
			} else {
				nextTimeLineVO.setType(TimelineTypeEnum.DISCUSS.getType());
				nextTimeLineVO.setDay(true);
				nextTimeLineVO.setDateCount(currentTimeLine.getDateCount());
				nextTimeLineVO.setTimes(Constants.DISCUSS_TIME);
				nextTimeLineVO.setTalkIndex(talkIndex);
			}
			break;
		case DISCUSS:
			//全部发言完才进入投票环节，否则继续下一个人发言
			nextTimeLineVO.setType(TimelineTypeEnum.VOTE.getType());
			break;
		case VOTE:
			//进入公布投票结果环节
			nextTimeLineVO.setType(TimelineTypeEnum.VOTE_RESULT.getType());
			break;
		case VOTE_RESULT:
			//如果有平票进入平票PK环节
			nextTimeLineVO.setType(TimelineTypeEnum.VOTE_PK.getType());
			
			//如果没有平票进入天黑环节
			nextTimeLineVO.setType(TimelineTypeEnum.DARK.getType());
			break;
		case VOTE_PK:
			//进入公布pk结果环节
			nextTimeLineVO.setType(TimelineTypeEnum.VOTE_PK_RESULT.getType());
			break;
		case VOTE_PK_RESULT:
			//进入天黑环节
			nextTimeLineVO.setType(TimelineTypeEnum.DARK.getType());
			nextTimeLineVO.setDay(false);
			break;
		default:
			break;
		}

		return nextTimeLineVO;
	}

	public static TimeLineVO wolfWinTimeLine() {
		TimeLineVO nextTimeLineVO = new TimeLineVO();
		nextTimeLineVO.setType(TimelineTypeEnum.WOLF_WIN.getType());
		return nextTimeLineVO;
	}

	public static TimeLineVO humanityWinTimeLine() {
		TimeLineVO nextTimeLineVO = new TimeLineVO();
		nextTimeLineVO.setType(TimelineTypeEnum.FARMER_WIN.getType());
		return nextTimeLineVO;
	}

}
