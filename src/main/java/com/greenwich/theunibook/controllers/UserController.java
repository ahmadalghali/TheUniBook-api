package com.greenwich.theunibook.controllers;

import com.greenwich.theunibook.dto.UserDTO;
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

    @GetMapping("/inactiveStaffCount")
    public int getInactiveStaffCount(@RequestParam int departmentId) {
        return userService.getInactiveStaff(departmentId).size();
    }

    @PostMapping("/register")
    public RegisterResponse register(@ModelAttribute RegisterRequest registerRequest) throws NoSuchAlgorithmException, InvalidKeySpecException {
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
    public HashMap<String, Object> encourageStaff(@RequestParam int departmentId) {
        return userService.encourageStaffToSubmitIdeas(departmentId);
    }

    @GetMapping("/users")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/department/{departmentId}")
    public List<UserDTO> getAllUsersByDepartment(@PathVariable("departmentId") int departmentId) {
        return userService.getAllUsersByDepartment(departmentId);
    }

    @GetMapping("/mostActiveUsers")
    public HashMap<String, Object> getMostActiveUsers(){
        return userService.getMostActiveUsers();
    }

    @GetMapping("/users/{userId}")
    public User getUser(@PathVariable("userId") int userId) {
        return userService.getUser(userId);
    }

    @PutMapping("/users/{userId}/disable")
    public HashMap<String, Object> disableAccount(@PathVariable("userId") int userId) {
        return userService.disableAccount(userId);
    }


    @PutMapping("/users/{userId}/enable")
    public HashMap<String, Object> enableAccount(@PathVariable("userId") int userId) {
        return userService.enableAccount(userId);
    }


    @PutMapping("/users/{userId}/disableAndHideActivity")
    public HashMap<String, Object> disableAccountAndHideActivity(@PathVariable("userId") int userId) {
        return userService.disableAccountAndHideActivity(userId);
    }

    @PutMapping("/users/{userId}/enableAndUnHideActivity")
    public HashMap<String, Object> enableAndUnHideActivity(@PathVariable("userId") int userId) {
        return userService.enableAndUnHideActivity(userId);
    }
}
