package com.example.mychat.repository;

import com.example.mychat.entity.ChatRoom;
import com.example.mychat.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(collectionResourceRel = "messages",path = "messages")
public interface MessageRepository extends JpaRepository<Message, Long> {

    @RestResource(path = "messagesInChat")
    Page<Message> findByChatRoom_IdOrderBySendTimeDesc(@Param("id") Long id, Pageable pageable);

//    Page<Message> findMessageByChatRoom(ChatRoom chatRoom,@PageableDefault(size = 10,page = 0,sort = "sendTime",direction = Sort.Direction.DESC ) Pageable pageable);


}