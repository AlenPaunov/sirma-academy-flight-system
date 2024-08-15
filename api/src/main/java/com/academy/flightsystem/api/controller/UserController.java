package com.academy.flightsystem.api.controller;

import com.academy.flightsystem.api.model.UserInfo;
import com.academy.flightsystem.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/getAll")
    public List<UserInfo> register(@RequestBody UserInfo userInfo){
        return new ArrayList<>();
    }

    @GetMapping("profile")
    public void getProfile(){
    }

}
