package com.kerellpnz.tnnwebdatabase.entity.general;

import com.kerellpnz.tnnwebdatabase.entity.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "inspectors")
public class Inspector extends BaseEntity {

    @Column(name="Name")
    private String name;

    @Column(name="Apointment")
    private String apointment;

    @Column(name="Subdivision")
    private String subdivision;

    @Column(name="Department")
    private String department;

    @Column(name="Login")
    private String login;

    @Column(name="Password")
    private String password;

    @Column(name="Role")
    private String role;

    @Column(name="Enabled")
    private boolean enabled;

    public Inspector() {

    }

    public Inspector(String name, String apointment, String subdivision, String department) {
        this.name = name;
        this.apointment = apointment;
        this.subdivision = subdivision;
        this.department = department;
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

    public String getSubdivision() {
        return subdivision;
    }

    public void setSubdivision(String subdivision) {
        this.subdivision = subdivision;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return getName();
    }
}
