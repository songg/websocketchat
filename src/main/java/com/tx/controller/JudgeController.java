package com.tx.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;
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
	
	Map<String, List<TimeLineVO>> timelines = new ConcurrentHashMap<String, List<TimeLineVO>>();
	
	@Autowired
	private RoomController roomController;

	/**
	 * 玩家行为: 
	 * 1. 狼人杀人
	 * 2. 预言家验身份
	 * 3. 守卫守护
	 * 4. 玩家投票
	 * 
	 * @param roomId
	 * @param user
	 */
	@MessageMapping("/op/{roomId}")
	public void playerOp(@DestinationVariable String roomId, String op) {
		List<TimeLineVO> timelineList = timelines.get(roomId);
		Room r = roomController.getRoomsCache().get(roomId);
		if(timelineList != null && timelineList.size() > 0) {
			TimeLineVO timeLine = timelineList.get(timelineList.size() - 1);
			
			PlayerOP playerOp = JSON.parseObject(op, PlayerOP.class);
			switch (PlayerOPEnum.from(playerOp.getOpType())) {
			case KILL:
			case GUARD:
				if(timeLine.getDeadIndex() != playerOp.getTargetIndex()) {
					timeLine.setDeadIndex(playerOp.getTargetIndex());
				}else {
					timeLine.setDeadIndex(0);
				}
				break;
			case IDENTIFY:
				UserVO player = r.getPlayer(playerOp.getTargetIndex());
				String seerDest = "/room/%s/%s/seer/%s";
				simpMessagingTemplate.convertAndSend(String.format(seerDest, r.getPrivateKey(), r.getRoomId(), player.getPrivateKey()), player.getRole());
				break;
			case VOTE:
				Map<Integer, Integer> voteMap = timeLine.getVoteOps();
				if(voteMap == null) {
					voteMap = new HashMap<>();
					timeLine.setVoteOps(voteMap);
				}
				voteMap.put(playerOp.getMyIndex(), playerOp.getTargetIndex());
				
			default:
				break;
			}
		}
	}
	
	
	/**
	 * 时间推进, 杀人结果, 游戏结果广播
	 * 天黑  / 天亮 杀人结果 / 
	 */
	@MessageMapping("/trigger/{roomId}")
	public void timeline(@DestinationVariable String roomId) {
		
		Room room = roomController.getRoomsCache().get(roomId);
		List<TimeLineVO> timeLines = timelines.get(roomId);
		String dest = "/room/%s/%s/timeline";
		
		TimeLineVO timeLine = null;
		//第一次触发，进入第一个天黑
		if(CollectionUtils.isEmpty(timeLines)) {
			timeLine = new TimeLineVO();
			timeLine.setDay(false);
			timeLine.setDeadIndex(0);
			timeLine.setType(1);
			timeLine.setVoteOps(null);
			timeLine.setDateCount(0);
		}else {
			timeLine = timeLines.get(timeLines.size() - 1);
		}
		simpMessagingTemplate.convertAndSend(String.format(dest, room.getPrivateKey(), roomId), timeLine);
		
		
		//填充下一个timeline
		TimeLineVO nextTimeLineVO = new TimeLineVO();
		timeLines.add(nextTimeLineVO);
		nextTimeLineVO.setDay(!timeLine.getDay());
		if(!timeLine.getDay()) {
			nextTimeLineVO.setDateCount(timeLines.size() / 2);
		}
		nextTimeLineVO.setDeadIndex(0);
		nextTimeLineVO.setType(1);
		nextTimeLineVO.setVoteOps(null);
	}

}
