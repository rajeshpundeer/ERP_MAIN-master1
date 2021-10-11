package com.school.iqdigit.Model;

import com.school.iqdigit.Modeldata.User;

public class LoginResponse {
    private boolean error;
    private String message;
    private User user;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private String phone;

    public LoginResponse(boolean error, String message, User user) {
        this.error = error;
        this.message = message;
        this.user = user;
    }
    public LoginResponse(boolean error,String message,String phone){
        this.error = error;
        this.message = message;
        this.phone = phone;
    }

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }
}
