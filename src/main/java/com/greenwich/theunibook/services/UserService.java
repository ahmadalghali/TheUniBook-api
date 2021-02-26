package com.greenwich.theunibook.services;

import com.greenwich.theunibook.dto.IdeaDTO;
import com.greenwich.theunibook.dto.UserDTO;
import com.greenwich.theunibook.models.Idea;
import com.greenwich.theunibook.models.User;
import com.greenwich.theunibook.repository.DepartmentRepository;
import com.greenwich.theunibook.repository.UserRepository;
import com.greenwich.theunibook.web.requests.LoginRequest;
import com.greenwich.theunibook.web.requests.RegisterRequest;
import com.greenwich.theunibook.web.responses.RegisterResponse;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    DepartmentRepository departmentRepository;


    private ModelMapper modelMapper = new ModelMapper();


    public RegisterResponse register(RegisterRequest registerRequest) {
        RegisterResponse registerResponse = new RegisterResponse();

        if (userRepository.findByEmail(registerRequest.getEmail()) != null) {
            registerResponse.setMessage("user exists");
            registerResponse.setUser(null);
        } else {
            User user = new User(registerRequest.getFirstname(), registerRequest.getLastname(),
                    registerRequest.getEmail(), registerRequest.getPassword(), registerRequest.getDepartmentId());
            user.setRole("Staff");
//            User user = new User(registerRequest.getEmail(), registerRequest.getPassword());

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


//    public LoginResponse login(LoginRequest loginRequest) {
//
//        LoginResponse loginResponse = new LoginResponse();
//
//        User user = userRepository.findByEmail(loginRequest.getEmail());
//
//        boolean userExists = user != null;
//
//
//        if (userExists) {
//            boolean passwordMatches = user.getPassword().trim().equals(loginRequest.getPassword());
//
//            if (passwordMatches) {
//
//                loginResponse.setUser(user);
//                loginResponse.setDepartment(departmentRepository.findById(user.getDepartmentId()).get());
//                loginResponse.setMessage("logged in");
//            } else {
//                loginResponse.setMessage("bad credentials");
//                loginResponse.setUser(null);
//            }
//
//        } else {
//            loginResponse.setMessage("bad credentials - user doesnt exist");
//            loginResponse.setUser(null);
//        }
//
//
//        return loginResponse;
//    }


    private UserDTO convertToUserْDTO(User user) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        userDTO.setDepartment(departmentRepository.findById(user.getDepartmentId()).get());

        return userDTO;
    }


    public HashMap<String, Object> login(LoginRequest loginRequest) {

        HashMap<String, Object> loginResponse = new HashMap();

        User user = userRepository.findByEmail(loginRequest.getEmail());

        boolean userExists = user != null;


        if (userExists) {
            boolean passwordMatches = user.getPassword().trim().equals(loginRequest.getPassword());

            if (passwordMatches) {

                loginResponse.put("user", convertToUserْDTO(user));
//                loginResponse.put("department", departmentRepository.findById(user.getDepartmentId()).get());
                loginResponse.put("message", "logged in");

            } else {

                loginResponse.put("message", "bad credentials");
            }

        } else {
            loginResponse.put("message", "bad credentials - user doesnt exist");
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
