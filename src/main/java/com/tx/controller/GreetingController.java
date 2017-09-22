package com.tx.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.tx.vo.Greeting;
import com.tx.vo.HelloMessage;

@Controller
public class GreetingController {
	
	@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public Greeting greeting(HelloMessage message) throws InterruptedException {
		Thread.sleep(1000);
		Greeting greeting = new Greeting("Hello From " + message.getName() + "!");
		return greeting;
	}
}
