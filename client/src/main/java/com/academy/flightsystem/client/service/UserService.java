package com.academy.flightsystem.client.service;

import com.academy.flightsystem.client.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserService {

    @Value("$backend.api.url")
    private String backendApiUrl;

    @Autowired
    private RestTemplate restTemplate;

    public void register(User user){
       restTemplate.postForObject(backendApiUrl + "/users/register", user,User.class);
    }
}
