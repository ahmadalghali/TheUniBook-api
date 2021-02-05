package com.greenwich.theunibook.services;

import com.greenwich.theunibook.models.User;
import com.greenwich.theunibook.repository.UserRepository;
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


    public RegisterResponse register(RegisterRequest registerRequest)
    {
        RegisterResponse registerResponse = new RegisterResponse();
        User user = new User(registerRequest.getFirstName(), registerRequest.getLastName(), registerRequest.getUserName(),
                             registerRequest.getPassword(), registerRequest.getEmail(), -1, -1);

        try
        {
            User savedUser = userRepository.save(user);

            registerResponse.setUser(savedUser);
            registerResponse.setMessage("registered");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            registerResponse.setUser(null);
            registerResponse.setMessage("register failed");
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
