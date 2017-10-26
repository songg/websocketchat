package com.tx.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.alibaba.fastjson.JSON;
import com.tx.model.constant.PlayerOPEnum;
import com.tx.vo.PlayerOP;
import com.tx.vo.Room;
import com.tx.vo.TimeLineVO;
import com.tx.vo.UserVO;

public class JudgeController {

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	private RoomController roomController;

	/**
	 * 玩家行为: 1. 狼人杀人 2. 预言家验身份 3. 守卫守护 4. 玩家投票
	 * 
	 * @param roomId
	 * @param user
	 */
	@MessageMapping("/op/{roomId}")
	public void playerOp(@DestinationVariable String roomId, String op) {
		Room r = roomController.getRoomsCache().get(roomId);
		List<TimeLineVO> timelineList = r.getTimelines();
		if (timelineList != null && timelineList.size() > 0) {
			TimeLineVO timeLine = timelineList.get(timelineList.size() - 1);

			PlayerOP playerOp = JSON.parseObject(op, PlayerOP.class);
			synchronized (timeLine) {
				switch (PlayerOPEnum.from(playerOp.getOpType())) {
				case START:
					//开始游戏，启动每个房间的法官，定时推进时间轴
					r.startJudger(simpMessagingTemplate);
					break;
				case KILL:
					//添加被杀对象
					timeLine.getKillIndex().add(playerOp.getTargetIndex());
					int killIndex = 0;
					for(int ki : timeLine.getKillIndex()) {
						if(ki > 0) {
							killIndex = ki;
							break;
						}
					}

					//狼人杀人优先杀先提交的，只适合6人局，2狼人
					if(killIndex > 0) {
						if (timeLine.getGuardIndex() != playerOp.getTargetIndex()) {
							timeLine.setDeadIndex(playerOp.getTargetIndex());
							r.addDeadNum(timeLine.getDeadIndex());
							r.getLiveIndex().remove(Integer.valueOf(timeLine.getDeadIndex()));
						}
					}
					break;
				case GUARD:
					timeLine.setGuardIndex(playerOp.getTargetIndex());
					if (timeLine.getDeadIndex() == playerOp.getTargetIndex()) {
						timeLine.setDeadIndex(0);
						r.reduceDeadNum(timeLine.getDeadIndex());
						r.getLiveIndex().add(timeLine.getDeadIndex());
					} 
					break;
				case IDENTIFY:
					timeLine.setIdentifyIndex(playerOp.getTargetIndex());
					UserVO player = r.getPlayer(playerOp.getTargetIndex());
					String seerDest = "/room/%s/%s/seer/%s";
					simpMessagingTemplate.convertAndSend(
							String.format(seerDest, r.getPrivateKey(), r.getRoomId(), player.getPrivateKey()),
							player.getRole());
					break;
				case VOTE:
					Map<Integer, Integer> voteMap = timeLine.getVoteOps();
					if (voteMap == null) {
						voteMap = new HashMap<>();
						timeLine.setVoteOps(voteMap);
					}
					voteMap.put(playerOp.getMyIndex(), playerOp.getTargetIndex());
					Map<Object, List<Entry<Integer, Integer>>> voteResultMap = voteMap.entrySet().stream()
							.collect(Collectors.groupingBy(e -> e.getValue()));
					
					//得票排序
					Set<Entry<Object, List<Entry<Integer, Integer>>>> sortedEntrySet = voteResultMap.entrySet().stream()
							.sorted((p1, p2) -> p1.getValue().size() - p2.getValue().size()).collect(Collectors.toSet());
					
					int top = 0;
					int second = 0;
					int topDeadIndex = 0;
					int secondDeadIndex = 0;
					for(Entry<Object, List<Entry<Integer, Integer>>> entry: sortedEntrySet) {
						if(top != 0 && second != 0) {
							break;
						}
						
						if(Integer.valueOf(entry.getKey().toString()) == 0 ) {
							//弃票跳过
							continue;
						}else if(top == 0) {
							top = entry.getValue().size();
							topDeadIndex = Integer.valueOf(entry.getKey().toString());
						}else if(second == 0) {
							second = entry.getValue().size();
							secondDeadIndex = Integer.valueOf(entry.getKey().toString());
						}
					}
					
					//判断是否平票
					if(top == second) {
						timeLine.setDeadIndex(0);
						List<Integer> pk = new ArrayList<>(2);
						pk.add(topDeadIndex);
						pk.add(secondDeadIndex);
						timeLine.setPk(pk);
					}else {
						timeLine.setDeadIndex(topDeadIndex);
						r.addDeadNum(timeLine.getDeadIndex());
						r.getLiveIndex().remove(Integer.valueOf(timeLine.getDeadIndex()));
					}
					
					break;
				default:
					break;
				}
			}
		}
	}

//	/**
//	 * 时间推进，参考{@link TimelineTypeEnum}
//	 */
//	@MessageMapping("/trigger/{roomId}")
//	public void timeline(@DestinationVariable String roomId) {
//
//		Room room = roomController.getRoomsCache().get(roomId);
//		List<TimeLineVO> timeLines = room.getTimelines();
//		String dest = "/room/%s/%s/timeline";
//
//		TimeLineVO timeLine = null;
//		// 第一次触发，进入第一个天黑
//		if (CollectionUtils.isEmpty(timeLines)) {
//			timeLine = new TimeLineVO();
//			timeLine.setDay(false);
//			timeLine.setDeadIndex(0);
//			timeLine.setType(TimelineTypeEnum.DARK.getType());
//			timeLine.setVoteOps(null);
//			timeLine.setDateCount(0);
//			timeLine.setTimes(TimelineTypeEnum.DARK.getTimes());
//			timeLines.add(timeLine);
//		} else {
//			timeLine = timeLines.get(timeLines.size() - 1);
//		}
//
//		simpMessagingTemplate.convertAndSend(String.format(dest, room.getPrivateKey(), roomId), timeLine);
//		
//		//下一个timeline为狼人获胜
//		if(room.getHumanityDeadNum() == 2) {
//			timeLines.add(TimeLineTrigger.wolfWinTimeLine());
//		}
//		
//		//下一个timeline为平民获胜
//		if(room.getWolfDeadNum() == 2) {
//			timeLines.add(TimeLineTrigger.humanityWinTimeLine());
//		}
// 		
//		if(timeLine.getType() != TimelineTypeEnum.WOLF_WIN.getType() || timeLine.getType() != TimelineTypeEnum.FARMER_WIN.getType()) {
//			// 填充下一个timeline
//			timeLines.add(TimeLineTrigger.nextTimeLine(timeLine, room));
//		}
//	}
}
