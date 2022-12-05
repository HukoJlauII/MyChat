package com.example.mychat.repository;

import com.example.mychat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RepositoryRestResource(collectionResourceRel = "rooms", path = "rooms")
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @RestResource(path = "findWithSearch")
    List<ChatRoom> findByRoomNameStartsWithIgnoreCase(String roomName);

    ChatRoom findChatRoomById(Long id);


}