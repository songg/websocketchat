package com.tx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.tx.vo.TimeLineVO;

public class JudgeController {
	
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

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
	@MessageMapping("/quit/{roomId}/op")
	public void playerOp() {
		
	}
	
	
	/**
	 * 时间推进, 杀人结果, 游戏结果广播
	 */
	@MessageMapping("/trigger/{roomId}")
	public void timeline(@DestinationVariable String roomId, int day) {
		String dest = "/room/%s/timeline";
		TimeLineVO timeLine = new TimeLineVO();
		simpMessagingTemplate.convertAndSend(String.format(dest, roomId), timeLine);
	}
}
