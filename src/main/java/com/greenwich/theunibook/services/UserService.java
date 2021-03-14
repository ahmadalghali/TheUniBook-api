package com.greenwich.theunibook.services;

import com.greenwich.theunibook.dto.UserDTO;
import com.greenwich.theunibook.enums.UserRole;
import com.greenwich.theunibook.models.User;
import com.greenwich.theunibook.repository.DepartmentRepository;
import com.greenwich.theunibook.repository.UserRepository;
import com.greenwich.theunibook.web.requests.LoginRequest;
import com.greenwich.theunibook.web.requests.RegisterRequest;
import com.greenwich.theunibook.web.responses.RegisterResponse;
import org.apache.commons.validator.routines.EmailValidator;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    private JavaMailSender sender;

    private ModelMapper modelMapper = new ModelMapper();

    public boolean isAuthenticated(String email, String password) {
        try {

            if (passwordMatches(email, password)) {
                return true;
            }
            return false;

        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAuthorized(String email, String password, UserRole role) {
        try {

            if (isAuthenticated(email, password)) {
                User authenticatedUser = userRepository.findByEmail(email);

                if (authenticatedUser.getRole() == role) {
                    return true;
                }
            }

            return false;

        } catch (Exception e) {
            return false;
        }
    }

    public RegisterResponse register(RegisterRequest registerRequest) throws NoSuchAlgorithmException, InvalidKeySpecException {
        RegisterResponse registerResponse = new RegisterResponse();

        String inputPassword = registerRequest.getPassword();
        String hashedPassword = generatePasswordHash(inputPassword);

        if (userRepository.findByEmail(registerRequest.getEmail()) != null) {
            registerResponse.setMessage("user exists");
            registerResponse.setUser(null);
        } else {
            User user = new User(registerRequest.getFirstname(), registerRequest.getLastname(),
                    registerRequest.getEmail(), hashedPassword, registerRequest.getDepartmentId());
            user.setRole(UserRole.STAFF);
            user.setProfileImageUrl(registerRequest.getProfileImageUrl());
            user.setEnabled(true);
            user.setHidden(false);
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

                LocalDateTime lastLogin = user.setLastLogin(LocalDateTime.now());
                userRepository.lastLoginDate(user.getEmail(), lastLogin);

            } else {

                loginResponse.put("message", "bad credentials");
            }

        } else {
            loginResponse.put("message", "bad credentials - user doesnt exist");
        }

        return loginResponse;
    }
    

    public boolean passwordMatches(String email, String password) {
        String dbPassword = userRepository.findByEmail(email).getPassword();
        if (dbPassword.equals(password)) {
            return true;
        }
        return false;
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
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }


    public HashMap<String, Object> sendResetPasswordEmail(String email) throws InvalidKeySpecException, NoSuchAlgorithmException {
        HashMap<String, Object> sendGeneratedPasswordResponse = new HashMap<>();

        User user = userRepository.findByEmail(email);
        String generatedPassword = generateRandomPassword();
        String hashPassword = generatePasswordHash(generatedPassword);

        userRepository.changePasswordWithEmail(hashPassword, user.getEmail());

        EmailValidator emailValidator = EmailValidator.getInstance();
        if (emailValidator.isValid(user.getEmail())) {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setFrom("theunibook1@gmail.com");
            mail.setTo(user.getEmail());
            mail.setSubject("Reset password");
            mail.setText("\n\n Hi, " + user.getFirstname() + "\n\nThis is your new password: \n" + generatedPassword + " \nhttps://theunibook.netlify.app\n\n\nThanks,\nTheUniBook Team");
            this.sender.send(mail);

            sendGeneratedPasswordResponse.put("message", "email sent");
        } else {
            sendGeneratedPasswordResponse.put("message", "email invalid");
        }

        return sendGeneratedPasswordResponse;
    }

    public String generateRandomPassword() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();

        return generatedString;
    }


    public HashMap<String, Object> changePassword(String oldPassword, String newPassword, String confirmPassword, int userId) throws InvalidKeySpecException, NoSuchAlgorithmException {
        HashMap<String, Object> changePasswordResponse = new HashMap<>();

        User user = userRepository.findById(userId).get();
        String dbUserHashedPassword = user.getPassword();
        String hashOldPassword = generatePasswordHash(oldPassword);
        String hashNewPassword = generatePasswordHash(newPassword);
        String hashConfirmPassword = generatePasswordHash(confirmPassword);

        boolean matched = hashConfirmPassword.equals(hashNewPassword);
        boolean samePassword = hashOldPassword.equals(hashNewPassword);

        if (samePassword) {
            changePasswordResponse.put("message", "New password cannot be the same as old password");
        } else if (dbUserHashedPassword.equals(hashOldPassword) && matched) {
            userRepository.changePassword(hashNewPassword, user.getId());
            changePasswordResponse.put("message", "Password changed");
        } else {
            changePasswordResponse.put("message", "Password not changed");
        }

        return changePasswordResponse;
    }


    public List<User> getInactiveStaff(int departmentId) {

        List<User> allUsersInDepartment = userRepository.getAllUsersInDepartment(departmentId);
        List<Integer> allUserIdsWithIdeasInDepartment = userRepository.getAllUserIdsInIdeas(departmentId);
        List<User> usersWithoutIdeas = new ArrayList<>();

        for (User user : allUsersInDepartment) {
            if (user.getRole() == UserRole.COORDINATOR) {
                continue;
            }
            if (!allUserIdsWithIdeasInDepartment.contains(user.getId())) {
                usersWithoutIdeas.add(user);
            }
        }

        return usersWithoutIdeas;
    }

    public HashMap<String, Object> encourageStaffToSubmitIdeas(int departmentId) {
        HashMap<String, Object> encourageStaffResponse = new HashMap<>();


//        List<User> allUsersInDepartment = userRepository.getAllUsersInDepartment(departmentId);
//        List<Integer> allUserIdsWithIdeasInDepartment = userRepository.getAllUserIdsInIdeas(departmentId);
//        List<User> usersWithoutIdeas = new ArrayList<>();
        List<User> usersWithoutIdeas = getInactiveStaff(departmentId);


//        for (User user : allUsersInDepartment) {
//            if (user.getRole() == UserRole.COORDINATOR) {
//                continue;
//            }
//            if (!allUserIdsWithIdeasInDepartment.contains(user.getId())) {
//
//                usersWithoutIdeas.add(user);
//            }
//        }

        try {
            int QACoordinatorId = userRepository.getQACoordinatorId(departmentId);
            String QACoordinatorName = userRepository.getQACoordinatorName(QACoordinatorId);

            for (User user : usersWithoutIdeas) {
                sendEmail(user, QACoordinatorName);
            }
            encourageStaffResponse.put("message", "email success");

        } catch (Exception e) {
            e.printStackTrace();
            encourageStaffResponse.put("message", "failed to send email");
        }

        return encourageStaffResponse;
    }

    private void sendEmail(User user, String qaCoordinatorName) {
        try {

            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(user.getEmail());
            mail.setSubject("Inactivity");
            mail.setText("\n\nHello " + user.getFirstname() + ",\n\nYou haven't been engaging recently.\nI hope everything is okay " + " \n\nhttps://theunibook.netlify.app\n\n\nKind regards,\n" + qaCoordinatorName);
            this.sender.send(mail);


        } catch (Exception e) {
            e.printStackTrace();

        }


    }

    private UserDTO convertToUserْDTO(User user) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        userDTO.setDepartment(departmentRepository.findById(user.getDepartmentId()).get());

        return userDTO;
    }

    private List<UserDTO> convertListToUserْDTO(List<User> users) {

        List<UserDTO> userDTOs = new ArrayList<>();
        for (User user : users) {
            userDTOs.add(convertToUserْDTO(user));
        }

        return userDTOs;
    }

    public List<UserDTO> getAllUsers() {

        return convertListToUserْDTO(userRepository.getAllUsers());
    }


    public User getUser(int userId) {
        return userRepository.findById(userId).get();
    }

    public List<UserDTO> getAllUsersByDepartment(int departmentId) {

        return convertListToUserْDTO(userRepository.getAllUsersInDepartment(departmentId));
    }

    public HashMap<String, Object> disableAccount(int userId) {
        HashMap<String, Object> response = new HashMap<>();

        User user = userRepository.findById(userId).get();
        if (!user.isEnabled()) {
            response.put("message", "user is disabled already");
        } else {
            response.put("message", "user account disabled");
            user.setEnabled(false);
            userRepository.save(user);
        }
        return response;
    }

    public HashMap<String, Object> enableAccount(int userId) {
        HashMap<String, Object> response = new HashMap<>();

        User user = userRepository.findById(userId).get();
        if (user.isEnabled()) {
            response.put("message", "user is enabled already");
        } else {
            response.put("message", "user account enabled");
            user.setEnabled(true);
            userRepository.save(user);
        }

        return response;
    }

    public HashMap<String, Object> disableAccountAndHideActivity(int userId) {
        HashMap<String, Object> response = new HashMap<>();

        try {
            User user = userRepository.findById(userId).get();

            user.setEnabled(false);
            user.setHidden(true);

            userRepository.save(user);


            response.put("message", "user account disabled and hidden");
            response.put("user", convertToUserْDTO(user));

        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "failed");

        }

        return response;
    }

    public HashMap<String, Object> enableAndUnHideActivity(int userId) {
        HashMap<String, Object> response = new HashMap<>();

        try {
            User user = userRepository.findById(userId).get();


            user.setHidden(false);
            user.setEnabled(true);

            userRepository.save(user);

            response.put("message", "user activity unhidden");
            response.put("user", convertToUserْDTO(user));


        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "failed");

        }

        return response;
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
