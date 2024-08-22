package com.academy.flightsystem.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String token;
    private Long expiresIn;
    private String username;
    private List<String> roles;
    private String error;


    public LoginResponse setToken(String token) {
        this.token = token;
        return this;
    }

    public LoginResponse setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }

    public LoginResponse setUsername(String username) {
        this.username = username;
        return this;
    }

    public LoginResponse setRoles(List<String> roles) {
        this.roles = roles;
        return this;
    }

    public LoginResponse setError(String error) {
        this.error = error;
        return this;
    }


}
