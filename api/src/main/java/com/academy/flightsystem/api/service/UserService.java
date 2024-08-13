package com.academy.flightsystem.api.service;

import com.academy.flightsystem.api.model.UserInfo;
import com.academy.flightsystem.api.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;
    public UserInfo register(UserInfo userInfo) {
        return userRepository.save(userInfo);
    }

}
