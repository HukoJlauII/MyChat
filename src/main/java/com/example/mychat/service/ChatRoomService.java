package com.example.mychat.service;

import com.example.mychat.entity.ChatRoom;
import com.example.mychat.repository.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatRoomService {
    @Autowired
    private ChatRoomRepository chatRoomRepository;

    public ChatRoom save(ChatRoom chatRoom) {
        return chatRoomRepository.save(chatRoom);
    }

    public void deleteRoomById(Long id) {
        chatRoomRepository.deleteById(id);
    }

    public ChatRoom findChatRoomById(Long id) {
        return chatRoomRepository.findChatRoomById(id);
    }


}
