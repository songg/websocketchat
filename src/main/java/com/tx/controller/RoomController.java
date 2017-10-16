package com.tx.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
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

import com.tx.tools.RolePicker;
import com.tx.vo.Greeting;
import com.tx.vo.HelloMessage;
import com.tx.vo.Room;
import com.tx.vo.RoomAndUser;
import com.tx.vo.UserVO;

@RestController
public class RoomController {

	Map<String, List<Room>> rooms = new ConcurrentHashMap<>();
	Map<String, Room> roomsCache = new ConcurrentHashMap<>();

	Set<String> mySet = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());

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
	 * 
	 * @param name
	 * @return
	 */
	@RequestMapping("/matching")
	public RoomAndUser queryRoom(String name) {
		List<Room> roomList = rooms.get("LOW");
		RoomAndUser roomAndUser = new RoomAndUser();
		boolean createNewRoom = true;
		if (CollectionUtils.isNotEmpty(roomList)) {
			// 加入已有房间
			for (Room r : roomList) {
				synchronized (r.getPlayers()) {
					if (r.getStatus() == 0 && r.getPlayers().size() < 6) {
						roomAndUser.setRoomId(r.getRoomId());
						UserVO lastPlayer = r.getPlayers().get(r.getPlayers().size() - 1);
						UserVO userVo = new UserVO();
						userVo.setName(name);
						userVo.setIndex(lastPlayer.getIndex() + 1);
						userVo.setLevel(10);
						userVo.setPrivateKey(UUID.randomUUID().toString());
						roomAndUser.setUser(userVo);
						roomAndUser.setRoomPrivateKey(r.getPrivateKey());
						r.getPlayers().add(userVo);
						createNewRoom = false;
						break;
					}
				}
			}
		}

		if (createNewRoom) {
			// 未匹配到房间，创建一个新房间
			Room r = new Room();
			roomList = new ArrayList<>();
			List<UserVO> players = new ArrayList<>();

			UserVO player = new UserVO();
			player.setIndex(1);
			player.setLevel(10);
			player.setName(name);
			player.setPrivateKey(UUID.randomUUID().toString());
			players.add(player);

			r.setPlayers(players);
			r.setStatus(0);
			r.setRoomId(String.valueOf(RandomUtils.nextLong()));
			r.setHolderIndex(player.getIndex());
			r.setPrivateKey(UUID.randomUUID().toString());
			roomList.add(r);
			rooms.put("LOW", roomList);
			roomsCache.put(String.valueOf(r.getRoomId()), r);
			roomAndUser.setRoomId(r.getRoomId());
			roomAndUser.setUser(player);
			roomAndUser.setRoomPrivateKey(r.getPrivateKey());
		}
		return roomAndUser;
	}

	/**
	 * 加入房间并在房间内广播加入用户
	 * 
	 * @param roomId
	 * @param user
	 */
	@MessageMapping("/join/{roomId}/{user}")
	public void join(@DestinationVariable String roomId, @DestinationVariable String user) {
		String dest = "/room/%s/%s";
		Room room = roomsCache.get(roomId);
		if (room != null) {
			List<UserVO> players = room.getPlayers();
			for (UserVO player : players) {
				if (player.getName().equals(user)) {
					simpMessagingTemplate.convertAndSend(String.format(dest, room.getPrivateKey(), roomId), room);
					break;
				}
			}

		}
	}

	/**
	 * 退出房间
	 * 
	 * @param roomId
	 * @param user
	 */
	@MessageMapping("/quit/{roomId}/{user}")
	public void quit(@DestinationVariable String roomId, @DestinationVariable String user) {
		Room room = roomsCache.get(roomId);
		List<UserVO> players = room.getPlayers();
		for (UserVO player : players) {
			if (player.getName().equals(user)) {
				players.remove(player);
				String dest = "/room/%s/%s";
				simpMessagingTemplate.convertAndSend(String.format(dest, room.getPrivateKey(), roomId), room);
				break;
			}
		}

	}

	/**
	 * 开始游戏
	 * 
	 * @param roomId
	 */
	@MessageMapping("/start/{roomId}")
	public void start(@DestinationVariable String roomId) {
		Room room = roomsCache.get(roomId);
		String dest = "/room/%s/%s/user/%s/%d";
		RolePicker rolePicker = new RolePicker();
		for (UserVO player : room.getPlayers()) {
			int role = rolePicker.pick();
			player.setRole(role);
			simpMessagingTemplate.convertAndSend(
					String.format(dest, room.getPrivateKey(), roomId, player.getPrivateKey(), player.getIndex()), role);
		}

	}

	public Map<String, Room> getRoomsCache() {
		return roomsCache;
	}
}
