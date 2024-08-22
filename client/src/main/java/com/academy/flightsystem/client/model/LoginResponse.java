package com.academy.flightsystem.client.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class LoginResponse {
    // Getters and setters
    private String token;
    private Long expiresIn;
    private String username;
    private List<String> roles;
    private String error;

}
