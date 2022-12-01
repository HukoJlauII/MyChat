package com.example.mychat.controller;

import com.example.mychat.entity.Message;
import com.example.mychat.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {
    @Autowired
    private MessageService messageService;

    @MessageMapping("chat.send/{roomId}")
    @SendTo("/topic/{roomId}")
    public Message sendMessage(@Payload Message message) {
        return messageService.save(message);
    }

    @MessageMapping("chat.delete/{roomId}")
    @SendTo("/topic/{roomId}")
    public void deleteMessage(@Payload Message message) {
        messageService.deleteMessageById(message.getId());
    }


}
