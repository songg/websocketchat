package com.tx.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tx.vo.Greeting;
import com.tx.vo.HelloMessage;
import com.tx.vo.Room;
import com.tx.vo.RoomAndUser;
import com.tx.vo.UserVO;

@RestController
public class RoomController {

	Map<String, List<Room>> rooms = new ConcurrentHashMap<>();
	Map<String, Room> roomsCache = new ConcurrentHashMap<>();
	
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public Greeting greeting(HelloMessage message) throws InterruptedException {
		Thread.sleep(1000);
		Greeting greeting = new Greeting("Hello From " + message.getName() + "!");
		return greeting;
	}

	/**
	 * 查找房间
	 * @param name
	 * @return
	 */
	@RequestMapping("/matching")
	public RoomAndUser queryRoom(String name) {
		List<Room> roomList = rooms.get("LOW");
		RoomAndUser roomAndUser = new RoomAndUser();
		
		if (CollectionUtils.isNotEmpty(roomList)) {
			for (Room r : roomList) {
				if(r.getStatus() == 0 && r.getPlayers().size() <6) {
					roomAndUser.setRoomId(r.getRoomId());
					boolean join = true;
					for(String player : r.getPlayers()) {
						if(player.equalsIgnoreCase(name)) {
							join = false;
							break;
						}
					}
					
					if(join) {
						UserVO userVo = new UserVO();
						userVo.setName(name);
						userVo.setIndex(index);
						
						roomAndUser.setUser(name);
						
						r.getPlayers().add(name);
					}
				}
			}
		}else {
			Room r = new Room();
			roomList = new ArrayList<>();
			List<String> players = new ArrayList<>();
			players.add(name);
			r.setPlayers(players);
			r.setStatus(0);
			r.setRoomId(String.valueOf(RandomUtils.nextLong()));
			roomList.add(r);
			rooms.put("LOW", roomList);
			roomsCache.put(String.valueOf(r.getRoomId()), r);
			roomAndUser.setRoomId(r.getRoomId());
		}
		return roomAndUser;
	}
	
	

	/**
	 * 加入房间并在房间内广播加入用户
	 * @param roomId
	 * @param user
	 */
	@MessageMapping("/join/{roomId}/{user}")
	public void join(@DestinationVariable String roomId, @DestinationVariable String user) {
		String  dest = "/room/" + roomId;
		Room room = roomsCache.get(roomId);
		simpMessagingTemplate.convertAndSend(dest, room.getPlayers());
	}
	
	
	/**
	 * 退出房间
	 * @param roomId
	 * @param user
	 */
	@MessageMapping("/quit/{roomId}/{user}")
	public void quit(@DestinationVariable String roomId, @DestinationVariable String user) {
		Room room = roomsCache.get(roomId);
		room.getPlayers().remove(user);
		String dest = "/room/" + roomId;
		simpMessagingTemplate.convertAndSend(dest, room.getPlayers());
	}
	
	
	@MessageMapping("/start/{roomId}")
	public void start(String roomId) {
		
	}
}
