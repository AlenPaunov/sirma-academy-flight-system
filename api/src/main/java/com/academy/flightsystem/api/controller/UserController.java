package com.academy.flightsystem.api.controller;

import com.academy.flightsystem.api.model.User;
import com.academy.flightsystem.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {
    // AuthenticationManager
    // jwtUtil
    // repo + service
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User register(@RequestBody User user){
        return userService.register(user);
    }

}
