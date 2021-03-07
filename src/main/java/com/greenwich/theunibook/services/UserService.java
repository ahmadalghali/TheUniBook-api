package com.greenwich.theunibook.services;

import com.greenwich.theunibook.dto.UserDTO;
import com.greenwich.theunibook.enums.UserRole;
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
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    DepartmentRepository departmentRepository;


    private ModelMapper modelMapper = new ModelMapper();


    public RegisterResponse register(RegisterRequest registerRequest) throws NoSuchAlgorithmException, InvalidKeySpecException {
        RegisterResponse registerResponse = new RegisterResponse();
        
        String inputPassword = registerRequest.getPassword() ;
        String hashedPassword = generatePasswordHash(inputPassword);
        
        if (userRepository.findByEmail(registerRequest.getEmail()) != null) {
            registerResponse.setMessage("user exists");
            registerResponse.setUser(null);
        } else {
            User user = new User(registerRequest.getFirstname(), registerRequest.getLastname(),
                    registerRequest.getEmail(), hashedPassword, registerRequest.getDepartmentId());
            user.setRole(UserRole.STAFF);
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

    public HashMap<String, Object> login(LoginRequest loginRequest) throws NoSuchAlgorithmException, InvalidKeySpecException {

        HashMap<String, Object> loginResponse = new HashMap();

        User user = userRepository.findByEmail(loginRequest.getEmail());

        boolean userExists = user != null;

        String dbUserHashedPassword = user.getPassword();

        String loginInputPassword = loginRequest.getPassword();
        String loginHashedPassword = generatePasswordHash(loginInputPassword);

        if (userExists) {
            boolean passwordMatches = dbUserHashedPassword.equals(loginHashedPassword);

            if (passwordMatches) {

                loginResponse.put("user", convertToUserْDTO(user));

                loginResponse.put("message", "logged in");

            } else {

                loginResponse.put("message", "bad credentials");
            }

        } else {
            loginResponse.put("message", "bad credentials - user doesnt exist");
        }

        return loginResponse;
    }

    private String generatePasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String generatedPassword = null;
        //password salting
        String fixedBytes = "17 f6 42 59 0d 09 5b 3c ba b7 f5 8b 36 bd 3c 15";
        byte[] salt = fixedBytes.getBytes(StandardCharsets.UTF_8);
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add the salt
            md.update(salt);

            byte[] bytes = md.digest(password.getBytes());

            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    private UserDTO convertToUserْDTO(User user) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        userDTO.setDepartment(departmentRepository.findById(user.getDepartmentId()).get());

        return userDTO;
    }

    public List<User> getAllUsers() {

        return userRepository.getAllUsers();
    }


    public User getUser(int userId) {
        return userRepository.findById(userId).get();
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

}
