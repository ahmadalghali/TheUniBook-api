package com.greenwich.theunibook.services;

import com.greenwich.theunibook.models.User;
import com.greenwich.theunibook.repository.UserRepository;
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


    public RegisterResponse register(User user) {

        RegisterResponse registerResponse = new RegisterResponse();

        User DBuser = userRepository.findByEmail(user.getEmail());

        if (DBuser != null) {
            registerResponse.setMessage("user exists");
            registerResponse.setUser(DBuser);

        } else {

//            userRepository.save(user);
            registerResponse.setUser(userRepository.save(user));
            registerResponse.setMessage("registered");

        }

        return registerResponse;
    }


    public LoginResponse login(User user) {

        User DBuser = userRepository.findByEmail(user.getEmail());

        LoginResponse loginResponse = new LoginResponse();

        if (DBuser != null) {

            if (DBuser.getPassword().equals(user.getPassword())) {
                loginResponse.setUser(DBuser);
                loginResponse.setMessage("logged in");

                return loginResponse;
            }

            loginResponse.setUser(DBuser);
            loginResponse.setMessage("incorrect password");

            return loginResponse;

        } else {
            user.setId(-1);
            loginResponse.setUser(user);
            loginResponse.setMessage("user does not exist");
            return loginResponse;
        }

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
