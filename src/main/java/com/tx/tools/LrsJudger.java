package com.tx.tools;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.tx.model.constant.TimelineTypeEnum;
import com.tx.vo.Room;
import com.tx.vo.TimeLineVO;

public class LrsJudger {
	private Room room;
	private SimpMessagingTemplate simpMessagingTemplate;
	private String dest = "/room/%s/%s/timeline";
	private Timer timer = new Timer();

	public LrsJudger(Room room, SimpMessagingTemplate simpMessagingTemplate) {
		this.room = room;
		this.simpMessagingTemplate = simpMessagingTemplate;
	}

	public void start() {
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

		//每轮预留1s的窗口期等待用户行为上报
		timer.schedule(new TimeLineTriggerTask(), timeLines.get(timeLines.size() - 1).getTimes() * 1000 + 1 * 1000);

	}
	
	class TimeLineTriggerTask extends TimerTask {
		
		@Override
		public void run() {
			List<TimeLineVO> timeLines = room.getTimelines();

			TimeLineVO timeLine = timeLines.get(timeLines.size() - 1);
			
			if(timeLine.getType() == TimelineTypeEnum.WOLF_WIN.getType() || timeLine.getType() == TimelineTypeEnum.FARMER_WIN.getType()) {
				return;
			}

			// 下一个timeline为狼人获胜
			if (room.getHumanityDeadNum() == 2) {
				timeLines.add(TimeLineTrigger.wolfWinTimeLine());
			}

			// 下一个timeline为平民获胜
			if (room.getWolfDeadNum() == 2) {
				timeLines.add(TimeLineTrigger.humanityWinTimeLine());
			}

			if (timeLine.getType() != TimelineTypeEnum.WOLF_WIN.getType()
					|| timeLine.getType() != TimelineTypeEnum.FARMER_WIN.getType()) {
				// 填充下一个timeline
				timeLines.add(TimeLineTrigger.nextTimeLine(timeLine, room));
			}
			
			simpMessagingTemplate.convertAndSend(String.format(dest, room.getPrivateKey(), room.getRoomId()),
					timeLines.get(timeLines.size() - 1));
			
			//每轮预留1s的窗口期等待用户行为上报
			timer.schedule(this, timeLines.get(timeLines.size() - 1).getTimes() * 1000 + 1 * 1000);
		}
		
	}  
}
