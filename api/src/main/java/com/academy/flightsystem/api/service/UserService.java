package com.academy.flightsystem.api.service;

import com.academy.flightsystem.api.model.User;
import com.academy.flightsystem.api.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;
    public User register(User user) {
        return userRepository.save(user);
    }

}
