package com.tx.tools;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.tx.model.constant.TimelineTypeEnum;
import com.tx.vo.Room;
import com.tx.vo.TimeLineVO;

public class LrsJudger {
	private Room room;
	private SimpMessagingTemplate simpMessagingTemplate;

	public LrsJudger(Room room, SimpMessagingTemplate simpMessagingTemplate) {
		this.room = room;
		this.simpMessagingTemplate = simpMessagingTemplate;
	}

	public void start() {
		Timer timer = new Timer();
		String dest = "/room/%s/%s/timeline";
		List<TimeLineVO> timeLines = room.getTimelines();
		TimeLineVO timeLine = new TimeLineVO();
		timeLine.setDay(false);
		timeLine.setDeadIndex(0);
		timeLine.setType(TimelineTypeEnum.DARK.getType());
		timeLine.setVoteOps(null);
		timeLine.setDateCount(0);
		timeLine.setTimes(TimelineTypeEnum.DARK.getTimes());
		timeLines.add(timeLine);
		simpMessagingTemplate.convertAndSend(String.format(dest, room.getPrivateKey(), room.getRoomId()), timeLine);
		
//		timer.
//		timer.schedule(new TimerTask() {
//			@Override
//			public void run() {
//				List<TimeLineVO> timeLines = room.getTimelines();
//				String dest = "/room/%s/%s/timeline";
//
//				TimeLineVO timeLine = null;
//				// 第一次触发，进入第一个天黑
//				if (CollectionUtils.isEmpty(timeLines)) {
//					timeLine = new TimeLineVO();
//					timeLine.setDay(false);
//					timeLine.setDeadIndex(0);
//					timeLine.setType(TimelineTypeEnum.DARK.getType());
//					timeLine.setVoteOps(null);
//					timeLine.setDateCount(0);
//					timeLine.setTimes(TimelineTypeEnum.DARK.getTimes());
//					timeLines.add(timeLine);
//				} else {
//					timeLine = timeLines.get(timeLines.size() - 1);
//				}
//
//				simpMessagingTemplate.convertAndSend(String.format(dest, room.getPrivateKey(), room.getRoomId()), timeLine);
//
//				// 下一个timeline为狼人获胜
//				if (room.getHumanityDeadNum() == 2) {
//					timeLines.add(TimeLineTrigger.wolfWinTimeLine());
//				}
//
//				// 下一个timeline为平民获胜
//				if (room.getWolfDeadNum() == 2) {
//					timeLines.add(TimeLineTrigger.humanityWinTimeLine());
//				}
//
//				if (timeLine.getType() != TimelineTypeEnum.WOLF_WIN.getType()
//						|| timeLine.getType() != TimelineTypeEnum.FARMER_WIN.getType()) {
//					// 填充下一个timeline
//					timeLines.add(TimeLineTrigger.nextTimeLine(timeLine, room));
//				}
//			}
//		}, room.getTimelines().get(index));
		
		
	}

}
