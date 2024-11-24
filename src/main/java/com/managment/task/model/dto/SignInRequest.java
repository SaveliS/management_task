package com.managment.task.model.dto;

public class SignInRequest {
    private String login;
    private String password;

    public SignInRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public SignInRequest() {
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

    @Override
    public String toString() {
        return "SignInRequest [login=" + login + ", password=" + password + "]";
    }
}
