package com.greenwich.theunibook.web.responses;


import com.greenwich.theunibook.models.Department;
import com.greenwich.theunibook.models.User;

public class LoginResponse {

    private User user;
    private String message;
    private Department department;


    public LoginResponse() {

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}
