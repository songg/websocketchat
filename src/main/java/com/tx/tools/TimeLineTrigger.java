package com.tx.tools;

import org.apache.commons.collections.CollectionUtils;

import com.tx.model.constant.Constants;
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
				nextTimeLineVO.setTimes(Constants.DISCUSS_TIME);
			} else {
				nextTimeLineVO.setType(TimelineTypeEnum.VOTE.getType());
				nextTimeLineVO.setTimes(Constants.VOTE_TIME);
			}
			nextTimeLineVO.setDateCount(currentTimeLine.getDateCount());
			nextTimeLineVO.setDay(true);
			break;
		case VOTE:
			// 进入公布投票结果环节
			nextTimeLineVO.setType(TimelineTypeEnum.VOTE_RESULT.getType());
			nextTimeLineVO.setDateCount(currentTimeLine.getDateCount());
			nextTimeLineVO.setDay(true);
			break;
		case VOTE_RESULT:
			// 如果有平票进入平票PK环节
			if (CollectionUtils.isNotEmpty(currentTimeLine.getPk())) {
				nextTimeLineVO.setType(TimelineTypeEnum.PK.getType());
				nextTimeLineVO.setDateCount(currentTimeLine.getDateCount());
				nextTimeLineVO.setDay(true);
				nextTimeLineVO.setTimes(Constants.PK_TIME);
				nextTimeLineVO.setPk(currentTimeLine.getPk());
			} else {
				// 如果没有平票进入天黑环节
				nextTimeLineVO.setType(TimelineTypeEnum.DARK.getType());
				nextTimeLineVO.setDay(false);
				nextTimeLineVO.setDateCount(currentTimeLine.getDateCount());
				// 重置第二天可以自由讨论的人
				r.setCanTalkIndex(r.getLiveIndex());
				nextTimeLineVO.setTimes(Constants.DARK_TIME);
			}
			break;
		case PK:
			// 进入公布pk结果环节
			nextTimeLineVO.setType(TimelineTypeEnum.PK_DISCUSS.getType());
			break;
		case PK_DISCUSS:
			//未发言完下一个人发言
			nextTimeLineVO.setType(TimelineTypeEnum.PK_DISCUSS.getType());
			
			//发言完进入PK_VOTE阶段
			nextTimeLineVO.setType(TimelineTypeEnum.PK_VOTE.getType());
			break;
		case PK_VOTE:
			//进入PK投票结果阶段
			nextTimeLineVO.setType(TimelineTypeEnum.PK_VOTE_RESULT.getType());
			break;
		case PK_VOTE_RESULT:
			// 进入天黑环节
			nextTimeLineVO.setType(TimelineTypeEnum.DARK.getType());
			nextTimeLineVO.setDateCount(currentTimeLine.getDateCount());
			nextTimeLineVO.setDay(false);

			// 重置第二天可以自由讨论的人
			r.setCanTalkIndex(r.getLiveIndex());

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
