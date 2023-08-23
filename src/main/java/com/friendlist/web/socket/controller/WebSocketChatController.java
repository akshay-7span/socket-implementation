package com.friendlist.web.socket.controller;

import com.friendlist.web.socket.model.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketChatController
{

	@MessageMapping("/sender")
	@SendTo("/receiver/message")
	public Message sendMessage(@Payload Message message)
	{
		System.out.printf("message" + message);
		return message;
	}
}