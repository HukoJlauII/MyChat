package com.example.mychat.controller;

import com.example.mychat.entity.Media;
import com.example.mychat.entity.User;
import com.example.mychat.repository.MediaRepository;
import com.example.mychat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Controller
public class ImageController {
    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private UserService userService;

    @ResponseBody
    @GetMapping("/image/{id}")
    private ResponseEntity<?> getImageById(@PathVariable Long id) {
        Media image = mediaRepository.findMediaById(id);
        return ResponseEntity.ok()
                .header("fileName", image.getOriginalFileName())
                .contentType(MediaType.valueOf(image.getMediaType()))
                .contentLength(image.getSize())
                .body(new InputStreamResource(new ByteArrayInputStream(image.getBytes())));
    }

    @PostMapping("/image/upload")
    private String uploadPhoto(@AuthenticationPrincipal User user, @RequestParam("Image") MultipartFile file) throws IOException {
        if (file != null) {
            Media media = new Media(file.getOriginalFilename(), file.getSize(), file.getContentType(), file.getBytes());
            user.setAvatar(media);
            userService.save(user);
        }
        return "redirect:/profile";
    }
}
