package com.friendlist.web.socket.controller;

import com.friendlist.web.socket.model.Message;
import com.friendlist.web.socket.model.WebSocketChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketChatController {

	@Autowired
	SimpMessagingTemplate simpMessagingTemplate;

	@MessageMapping("/chat.sendMessage")
	@SendTo("/topic/friendlist")
	public WebSocketChatMessage sendMessage(@Payload WebSocketChatMessage webSocketChatMessage) {
		return webSocketChatMessage;
	}

	@MessageMapping("/chat.newUser")
	@SendTo("/topic/friendlist")
	public WebSocketChatMessage newUser(@Payload WebSocketChatMessage webSocketChatMessage,
			SimpMessageHeaderAccessor headerAccessor) {
		headerAccessor.getSessionAttributes().put("username", webSocketChatMessage.getSender());
		return webSocketChatMessage;
	}

	@MessageMapping("/notification.broadcast")
	@SendTo("/all/notification")
	public Message send(final Message message) {
		return message;
	}

	@MessageMapping("/notification.private")
	public void sendToSpecificUser(@Payload Message message) {
		simpMessagingTemplate.convertAndSendToUser(message.getTo(), "/specific", message);
	}

}