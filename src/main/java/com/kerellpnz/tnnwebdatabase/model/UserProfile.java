package com.kerellpnz.tnnwebdatabase.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserProfile {

    private int id;

    @NotNull(message = "Не может быть пустым!")
    @Size(min = 1, message = "Не может быть пустым!")
    private String login;

    @NotNull(message = "Не может быть пустым!")
    @Size(min = 1, message = "Не может быть пустым!")
    private String name;

    @NotNull(message = "Не может быть пустым!")
    @Size(min = 1, message = "Не может быть пустым!")
    private String apointment;

//    @ValidEmail
//    @NotNull(message = "is required")
//    @Size(min = 1, message = "is required")
//    private String email;

    public UserProfile() {}

    public UserProfile(UserModel userModel) {
        this.id = userModel.getId();
        this.login = userModel.getLogin();
        this.name = userModel.getName();
        this.apointment = userModel.getApointment();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApointment() {
        return apointment;
    }

    public void setApointment(String apointment) {
        this.apointment = apointment;
    }

//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
}
