package com.tx.controller;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;


@RestController
public class LobbyController {
	
	Set<String> onlineUsers = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
	
	
	@EventListener
	private void onSessionConnectedEvent(SessionConnectedEvent event) {
	    StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
	    onlineUsers.add(sha.getSessionId());
	}

	@EventListener
	private void onSessionDisconnectEvent(SessionDisconnectEvent event) {
	    StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
	    onlineUsers.remove(sha.getSessionId());
	}

	@RequestMapping("/lobby/onlinenum.do")
	public int getOnlineNum() {
		return onlineUsers.size();
	}
	
}
