package com.example.mychat.service;

import com.example.mychat.entity.ChatRoom;
import com.example.mychat.entity.Message;
import com.example.mychat.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    public Message save(Message message)
    {
        return messageRepository.save(message);
    }
//    Page<Message> findMessageByChatRoom(ChatRoom chatRoom, int page)
//    {
//        return messageRepository.findMessageByChatRoom(chatRoom, PageRequest.of(page,10));
//    }

    public void deleteMessageById(Long id){
        if (messageRepository.findById(id).isPresent()) {
            messageRepository.deleteById(id);
        }
    }


}
