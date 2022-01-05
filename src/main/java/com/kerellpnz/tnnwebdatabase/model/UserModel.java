package com.kerellpnz.tnnwebdatabase.model;

import java.io.Serializable;

public class UserModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String apointment;
    private String role;
    private String login;
    private String journalNumber;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getJournalNumber() {
        return journalNumber;
    }

    public void setJournalNumber(String journalNumber) {
        this.journalNumber = journalNumber;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", login='" + login + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
