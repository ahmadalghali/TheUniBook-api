package com.greenwich.theunibook.controllers;

import com.greenwich.theunibook.models.User;
import com.greenwich.theunibook.repository.UserRepository;
import com.greenwich.theunibook.services.UserService;
import com.greenwich.theunibook.web.requests.LoginRequest;
import com.greenwich.theunibook.web.requests.RegisterRequest;
import com.greenwich.theunibook.web.responses.RegisterResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

//    @GetMapping("/inactiveUserCount")
//    public int getInactiveUserCount(@RequestParam int departmentId){
//        return userRepository.
//    }

    @PostMapping("/register")
    public RegisterResponse register(@RequestBody RegisterRequest registerRequest) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return userService.register(registerRequest);
    }

    @PostMapping("/login")
    public HashMap<String, Object> login(@RequestBody LoginRequest loginRequest) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return userService.login(loginRequest);
    }

    @PostMapping("/forgottenPassword")
    public HashMap<String, Object> sendResetPasswordEmail(@RequestParam String email) throws InvalidKeySpecException, NoSuchAlgorithmException {
        return userService.sendResetPasswordEmail(email);
    }

    @PostMapping("/changePassword")
    public HashMap<String, Object> changePassword(@RequestParam String oldPassword, @RequestParam String newPassword, @RequestParam String confirmPassword, @RequestParam int userId ) throws InvalidKeySpecException, NoSuchAlgorithmException {
        return userService.changePassword(oldPassword, newPassword, confirmPassword, userId);
    }

    @PostMapping("/encourageStaff")
    public HashMap<String, Object> encourageStaff(@RequestParam int departmentId ) {
        return userService.encourageStaffToSubmitIdeas(departmentId);
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }


    @GetMapping("/users/{userId}")
    public User getUser(@PathVariable("userId") int userId) {
        return userService.getUser(userId);
    }

}
