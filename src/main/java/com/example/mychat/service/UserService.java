package com.example.mychat.service;

import com.example.mychat.entity.Role;
import com.example.mychat.entity.User;
import com.example.mychat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Objects;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Transactional
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }


    public void saveUser(User user) {
        User userFromDB = userRepository.findUserByUsername(user.getUsername());
        if (userFromDB != null) {
            return;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singleton(Role.ROLE_USER));
        user.setRegistrationDate(LocalDate.now());
        save(user);
    }

    public User getUserAuth() {
        User userAuth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findUserById(userAuth.getId());
    }

    public String validateRegister(User user, BindingResult bindingResult) {

        if (!Objects.equals(user.getPassword(), user.getPasswordConfirm())) {
            bindingResult.addError(new FieldError("user", "passwordConfirm", "Пароли не совпадают"));
        }
        if (findUserByUsername(user.getUsername()) != null) {
            bindingResult.addError(new FieldError("user", "username", "Пользователь с таким никнеймом уже существует"));
        }
        if (findUserByEmail(user.getEmail()) != null) {
            bindingResult.addError(new FieldError("user", "email", "Пользователь с такой почтой уже существует"));
        }
        if (bindingResult.hasErrors()) {
            return "/pages-register";
        }
        try {

            user.setRegistrationDate(LocalDate.now());
            saveUser(user);
            return "redirect:/login";
        } catch (Exception e) {
            return "/pages-register";
        }
    }

    public String editProfile(RedirectAttributes redirectAttributes,
                              User user, BindingResult bindingResult) {
        User oldUser = getUserAuth();

        if (findUserByEmail(user.getEmail()) != null && !oldUser.getEmail().equals(user.getEmail())) {
            bindingResult.addError(new FieldError("user", "email", "Пользователь с такой почтой уже существует"));
        }
        if (findUserByEmail(user.getUsername()) != null && !oldUser.getUsername().equals(user.getUsername())) {
            bindingResult.addError(new FieldError("user", "username", "Пользователь с таким никнеймом уже существует"));
        }
        if (bindingResult.hasErrors()) {
            return "/users-profile";
        }
        oldUser.setName(user.getName());
        oldUser.setSurname(user.getSurname());
        oldUser.setUsername(user.getUsername());
        oldUser.setEmail(user.getEmail());
        save(oldUser);
        redirectAttributes.addFlashAttribute("profileChanged", true);
        return "redirect:/profile";
    }

    public String changePassword(User user, String currentPassword, String newPassword,
                                 String passwordConfirm, RedirectAttributes redirectAttributes) {
        if (!Objects.equals(currentPassword, "") && !Objects.equals(newPassword, "")
                && !Objects.equals(passwordConfirm, "")) {
            if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
                redirectAttributes.addFlashAttribute("errorCurrentPassword", true);
                return "redirect:/profile#profile-change-password";
            }

            if (!Objects.equals(newPassword, passwordConfirm)) {
                redirectAttributes.addFlashAttribute("errorPassword", true);
                return "redirect:/profile#profile-change-password";
            }

            user.setPassword(passwordEncoder.encode(newPassword));
            redirectAttributes.addFlashAttribute("passwordChanged", true);
            save(user);

        }
        return "redirect:/profile#profile-change-password";
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        } else return user;
    }
}
