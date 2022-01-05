package com.kerellpnz.tnnwebdatabase.model;

import com.kerellpnz.tnnwebdatabase.validation.FieldMatch;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@FieldMatch.List({
        @FieldMatch(first = "password", second = "matchingPassword", message = "Пароли не совпадают!")
})
public class ChangePassword {

    private int id;

    @NotNull(message = "Не может быть пустым!")
    @Size(min = 8, message = "Не менее 8 символов")
    private String password;

    @NotNull(message = "Не может быть пустым!")
    @Size(min = 8, message = "Не менее 8 символов")
    private String matchingPassword;

    public ChangePassword() {}

    public ChangePassword(UserModel userModel) {
        this.id = userModel.getId();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }
}
