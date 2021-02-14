package com.greenwich.theunibook.services;

import com.greenwich.theunibook.models.User;
import com.greenwich.theunibook.repository.UserRepository;
import com.greenwich.theunibook.web.requests.LoginRequest;
import com.greenwich.theunibook.web.requests.RegisterRequest;
import com.greenwich.theunibook.web.responses.LoginResponse;
import com.greenwich.theunibook.web.responses.RegisterResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;


    public RegisterResponse register(RegisterRequest registerRequest) {
        RegisterResponse registerResponse = new RegisterResponse();

        if (userRepository.findByEmail(registerRequest.getEmail()) != null && userRepository.findByUsername(registerRequest.getUserName()) != null) {
            registerResponse.setMessage("user exists");
            registerResponse.setUser(null);
        } else {
//            User user = new User(registerRequest.getFirstName(), registerRequest.getLastName(), registerRequest.getUserName(),
//                    registerRequest.getPassword(), registerRequest.getEmail(), 1, 1);
            User user = new User(registerRequest.getEmail(), registerRequest.getPassword());

            try {
                User savedUser = userRepository.save(user);

                registerResponse.setUser(savedUser);
                registerResponse.setMessage("registered");
            } catch (Exception e) {
                e.printStackTrace();
                registerResponse.setUser(null);
                registerResponse.setMessage("register failed");
            }

        }
        return registerResponse;
    }


    public LoginResponse login(LoginRequest loginRequest) {

        LoginResponse loginResponse = new LoginResponse();

        User user = userRepository.findByEmail(loginRequest.getEmail());

        boolean userExists = user != null;


        if (userExists) {
           boolean passwordMatches = user.getPassword().trim().equals(loginRequest.getPassword());

            if (passwordMatches) {

                loginResponse.setUser(user);
                loginResponse.setMessage("logged in");
            } else {
                loginResponse.setMessage("bad credentials");
                loginResponse.setUser(null);
            }

        } else {
            loginResponse.setMessage("bad credentials - user doesnt exist");
            loginResponse.setUser(null);
        }


        return loginResponse;
    }


    public List<User> getAllUsers() {

        List<User> users = new ArrayList<>();

        userRepository.findAll().forEach(users::add);

        return users;
    }


    public User getUser(int userId) {
        return userRepository.findById(userId).get();
    }
}
