package com.example.mychat.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    private LocalDateTime sendTime;


    @RestResource(exported = false)
    @JsonIgnoreProperties(value = "owner", allowSetters = true)
    @OneToOne(targetEntity = ChatRoom.class, fetch = FetchType.EAGER)
    private ChatRoom chatRoom;

}
