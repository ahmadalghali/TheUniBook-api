package com.greenwich.theunibook.controllers;

import com.greenwich.theunibook.dto.UserDTO;
import com.greenwich.theunibook.enums.UserRole;
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
import java.util.Date;
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
    public List<UserDTO> getMostActiveUsers(@RequestParam String email, @RequestParam String password) {

        if (userService.isAuthorized(email, password, UserRole.ADMINISTRATOR)) {

            return userService.getMostActiveUsers();

        }
        return null;
    }

    @GetMapping("/users/{userId}")
    public User getUser(@PathVariable("userId") int userId) {
        return userService.getUser(userId);
    }

    @PutMapping("/users/{userId}/disable")
    public HashMap<String, Object> disableAccount(@RequestParam String email, @RequestParam String password, @PathVariable("userId") int userId) {

        HashMap<String, Object> response = new HashMap<>();

        if (userService.isAuthorized(email, password, UserRole.MANAGER)) {
            return userService.disableAccount(userId);

        } else {
            response.put("message", "user has no authorization for this action");
            return response;
        }


    }


    @PutMapping("/users/{userId}/enable")
    public HashMap<String, Object> enableAccount(@RequestParam String email, @RequestParam String password, @PathVariable("userId") int userId) {

        HashMap<String, Object> response = new HashMap<>();

        if (userService.isAuthorized(email, password, UserRole.MANAGER)) {
            return userService.enableAccount(userId);


        } else {
            response.put("message", "user has no authorization for this action");
            return response;
        }

    }


    @PutMapping("/users/{userId}/disableAndHideActivity")
    public HashMap<String, Object> disableAccountAndHideActivity(@RequestParam String email, @RequestParam String password, @PathVariable("userId") int userId) {

        HashMap<String, Object> response = new HashMap<>();

        if (userService.isAuthorized(email, password, UserRole.MANAGER)) {
            return userService.disableAccountAndHideActivity(userId);

        } else {
            response.put("message", "user has no authorization for this action");
            return response;
        }
    }

    @PutMapping("/users/{userId}/enableAndUnHideActivity")
    public HashMap<String, Object> enableAndUnHideActivity(@RequestParam String email, @RequestParam String password, @PathVariable("userId") int userId) {

        HashMap<String, Object> response = new HashMap<>();

        if (userService.isAuthorized(email, password, UserRole.MANAGER)) {
            return userService.enableAndUnHideActivity(userId);

        } else {
            response.put("message", "user has no authorization for this action");
            return response;
        }
    }



}
