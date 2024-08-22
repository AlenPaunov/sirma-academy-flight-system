package com.academy.flightsystem.client.controller;

import com.academy.flightsystem.client.model.LoginResponse;
import com.academy.flightsystem.client.model.User;
import com.academy.flightsystem.client.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService service;

    @Autowired
    private HttpSession session;

    @GetMapping("/register")
    public String registerPage(Model model){
        model.addAttribute("user", new User());
        return "register";

    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model){
        try {
            service.register(user);
            return "redirect:/login";
        } catch (Exception e){
            model.addAttribute("user", user);
            model.addAttribute("error", e.getMessage());
            return "register";
        }

    }

    @GetMapping("/login")
    public String loginPage(Model model){
        logger.info("get login page");
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, Model model){
        logger.info("Login attempt for user: {}", user.getUsername());

        try {
            service.login(user);
            logger.debug( "Login successful for user: {}", user.getUsername());
            return "redirect:/";
        } catch (Exception e) {
            logger.info("Login failed for user: {}", user.getUsername());
            logger.info("ERROR", e.getMessage());
            model.addAttribute("user", user);
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }


    @GetMapping("/user")
    public String testUserJWT(Model model){
        logger.info("get user page");
        service.testUserJWT();
        return "user";
    }


    @GetMapping("/admin")
    public String testAdminWT(Model model){
        logger.info("get admin page");
        service.testAdminJWT();
        return "admin";
    }



}
