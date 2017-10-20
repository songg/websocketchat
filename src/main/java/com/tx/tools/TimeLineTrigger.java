package com.tx.tools;

import org.apache.commons.collections.CollectionUtils;

import com.tx.model.constant.TimelineTypeEnum;
import com.tx.vo.Room;
import com.tx.vo.TimeLineVO;

/**
 * 各版本狼人杀通用的timeline推荐类
 * @author songgang
 *
 */
public class TimeLineTrigger {
	public static TimeLineVO nextTimeLine(TimeLineVO currentTimeLine, Room r) {
		TimeLineVO nextTimeLineVO = new TimeLineVO();
		switch (TimelineTypeEnum.from(currentTimeLine.getType())) {
		case DARK:
			// 当前阶段为天黑阶段，进入今晚用户行为结果广播阶段
			nextTimeLineVO.setType(TimelineTypeEnum.DARK_OP_RESULT.getType());
			nextTimeLineVO.setDay(true);
			nextTimeLineVO.setDateCount(currentTimeLine.getDateCount() + 1);
			nextTimeLineVO.setTimes(TimelineTypeEnum.DARK_OP_RESULT.getTimes());
			break;
		case DARK_OP_RESULT:
			// 当前阶段为广播昨晚用户行为结果阶段，进入遗言或者自由讨论时间
			if (currentTimeLine.getDeadIndex() > 0) {
				nextTimeLineVO.setType(TimelineTypeEnum.LAST_WORDS.getType());
				nextTimeLineVO.setDay(true);
				nextTimeLineVO.setTalkIndex(currentTimeLine.getDeadIndex());
				nextTimeLineVO.setDateCount(currentTimeLine.getDateCount());
				nextTimeLineVO.setTimes(TimelineTypeEnum.LAST_WORDS.getTimes());
			} else {
				nextTimeLineVO.setType(TimelineTypeEnum.DISCUSS.getType());
				nextTimeLineVO.setDay(true);
				nextTimeLineVO.setDateCount(currentTimeLine.getDateCount());
				nextTimeLineVO.setTimes(TimelineTypeEnum.DISCUSS.getTimes());
				nextTimeLineVO.setTalkIndex(r.getCanTalkIndex().get(0));
				r.getCanTalkIndex().remove(0);
			}
			break;
		case DISCUSS:
			// 全部发言完才进入投票环节，否则继续下一个人发言
			if (r.getCanTalkIndex().size() > 0) {
				nextTimeLineVO.setType(TimelineTypeEnum.DISCUSS.getType());
				nextTimeLineVO.setTalkIndex(r.getCanTalkIndex().get(0));
				r.getCanTalkIndex().remove(0);
				nextTimeLineVO.setTimes(TimelineTypeEnum.DISCUSS.getTimes());
			} else {
				nextTimeLineVO.setType(TimelineTypeEnum.VOTE.getType());
				nextTimeLineVO.setTimes(TimelineTypeEnum.VOTE.getTimes());
			}
			nextTimeLineVO.setDateCount(currentTimeLine.getDateCount());
			nextTimeLineVO.setDay(true);
			break;
		case VOTE:
			// 进入公布投票结果环节
			nextTimeLineVO.setType(TimelineTypeEnum.VOTE_RESULT.getType());
			nextTimeLineVO.setDateCount(currentTimeLine.getDateCount());
			nextTimeLineVO.setDay(true);
			nextTimeLineVO.setTimes(TimelineTypeEnum.VOTE_RESULT.getTimes());
			break;
		case VOTE_RESULT:
			// 如果有平票进入平票PK环节
			if (CollectionUtils.isNotEmpty(currentTimeLine.getPk())) {
				nextTimeLineVO.setType(TimelineTypeEnum.PK.getType());
				nextTimeLineVO.setDateCount(currentTimeLine.getDateCount());
				nextTimeLineVO.setDay(true);
				nextTimeLineVO.setTimes(TimelineTypeEnum.PK.getTimes());
				nextTimeLineVO.setPk(currentTimeLine.getPk());
			} else {
				// 如果没有平票进入天黑环节
				nextTimeLineVO.setType(TimelineTypeEnum.DARK.getType());
				nextTimeLineVO.setDay(false);
				nextTimeLineVO.setDateCount(currentTimeLine.getDateCount());
				// 重置第二天可以自由讨论的人
				r.setCanTalkIndex(r.getLiveIndex());
				nextTimeLineVO.setTimes(TimelineTypeEnum.DARK.getTimes());
			}
			break;
		case PK:
			// 进入pk第一人发言环节
			nextTimeLineVO.setType(TimelineTypeEnum.PK_DISCUSS_1.getType());
			nextTimeLineVO.setDateCount(currentTimeLine.getDateCount());
			nextTimeLineVO.setDay(true);
			nextTimeLineVO.setPk(currentTimeLine.getPk());
			nextTimeLineVO.setTalkIndex(currentTimeLine.getPk().get(0));
			nextTimeLineVO.setTimes(TimelineTypeEnum.PK_DISCUSS_1.getTimes());
			break;
		case PK_DISCUSS_1:
			//进入pk第二人发言环节
			nextTimeLineVO.setType(TimelineTypeEnum.PK_DISCUSS_2.getType());
			nextTimeLineVO.setDateCount(currentTimeLine.getDateCount());
			nextTimeLineVO.setDay(true);
			nextTimeLineVO.setPk(currentTimeLine.getPk());
			nextTimeLineVO.setTalkIndex(currentTimeLine.getPk().get(1));
			nextTimeLineVO.setTimes(TimelineTypeEnum.PK_DISCUSS_2.getTimes());
			break;
		case PK_DISCUSS_2:
			//发言完进入PK投票环节
			nextTimeLineVO.setType(TimelineTypeEnum.PK_VOTE.getType());
			nextTimeLineVO.setDateCount(currentTimeLine.getDateCount());
			nextTimeLineVO.setDay(true);
			nextTimeLineVO.setPk(currentTimeLine.getPk());
			nextTimeLineVO.setTimes(TimelineTypeEnum.PK_VOTE.getTimes());
			break;
		case PK_VOTE:
			//进入PK投票结果环节
			nextTimeLineVO.setType(TimelineTypeEnum.PK_VOTE_RESULT.getType());
			nextTimeLineVO.setDateCount(currentTimeLine.getDateCount());
			nextTimeLineVO.setDay(true);
			nextTimeLineVO.setTimes(TimelineTypeEnum.PK_VOTE_RESULT.getTimes());
			break;
		case PK_VOTE_RESULT:
			// 进入天黑环节
			nextTimeLineVO.setType(TimelineTypeEnum.DARK.getType());
			nextTimeLineVO.setDateCount(currentTimeLine.getDateCount());
			nextTimeLineVO.setDay(false);

			// 重置第二天可以自由讨论的人
			r.setCanTalkIndex(r.getLiveIndex());
			nextTimeLineVO.setTimes(TimelineTypeEnum.DARK.getTimes());
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
