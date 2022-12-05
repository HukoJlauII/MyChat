package com.example.mychat.service;

import com.example.mychat.entity.Message;
import com.example.mychat.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    public Message save(Message message) {
        return messageRepository.save(message);
    }


    public void deleteMessageById(Long id) {
        if (messageRepository.findById(id).isPresent()) {
            messageRepository.deleteById(id);
        }
    }


}
