package com.example.mychat.controller;

import com.example.mychat.entity.ChatRoom;
import com.example.mychat.entity.User;
import com.example.mychat.service.ChatRoomService;
import com.example.mychat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    private UserService userService;

    @Autowired
    private ChatRoomService chatRoomService;

    @GetMapping("")
    public String getChat() {
        return "chat";
    }

    @ResponseBody
    @PostMapping("/create")
    public User createChat(@RequestBody ChatRoom chatRoom) {
        User user = userService.getUserAuth();
        user.getRooms().add(chatRoom);
        return userService.save(user);
    }

    @ResponseBody
    @GetMapping("/check")
    public boolean checkUserInChat(@RequestParam("id") Long id) {
        User user = userService.getUserAuth();
        ChatRoom chatRoom = chatRoomService.findChatRoomById(id);
        if (!user.getRooms().contains(chatRoom)) {
            user.getRooms().add(chatRoom);
            userService.save(user);
            return true;
        } else {
            return false;
        }
    }
}
