package com.example.mychat.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "message")
@Table(name = "messages", schema = "jpa")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @RestResource(exported = false)
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "sender_id")
    private User sender;

    private String content;
    //    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sendTime;

    @RestResource(exported = false)
    @JsonIgnoreProperties(value ="owner",allowSetters = true)
    @OneToOne(targetEntity = ChatRoom.class, fetch = FetchType.EAGER)
    private ChatRoom chatRoom;

}
