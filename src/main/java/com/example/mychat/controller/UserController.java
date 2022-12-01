package com.example.mychat.controller;

import com.example.mychat.entity.User;
import com.example.mychat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String showIndex(Model model)
    {
        model.addAttribute("user",userService.getUserAuth());
        return "index";
    }

    @GetMapping("/profile")
    public String showProfile(Model model){
        model.addAttribute("user",userService.getUserAuth());
        User user =new User();
        model.addAttribute("newUser",user);
        return "users-profile";
    }

    @PostMapping("/edit")
    public String changeProfile(RedirectAttributes redirectAttributes, @ModelAttribute("newUser") @Valid User user, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "users-profile";
        }
        return userService.editProfile(redirectAttributes,user,bindingResult);
    }

    @PostMapping("/edit/password")
    public String changePassword(RedirectAttributes redirectAttributes,
                                 @RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("renewPassword") String passwordConfirm){
        User user=userService.getUserAuth();
        return userService.changePassword(user, currentPassword, newPassword, passwordConfirm, redirectAttributes);

    }
}
