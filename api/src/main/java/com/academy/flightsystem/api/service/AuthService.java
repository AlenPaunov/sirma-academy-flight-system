package com.academy.flightsystem.api.service;

import com.academy.flightsystem.api.model.UserInfo;
import com.academy.flightsystem.api.model.dto.LoginUserDto;
import com.academy.flightsystem.api.model.dto.RegisterUserDto;
import com.academy.flightsystem.api.repository.UserRepository;
import com.academy.flightsystem.api.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    public UserInfo register(RegisterUserDto userDto){
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(userDto.getUsername());
        userInfo.setPassword(passwordEncoder.encode(userDto.getPassword()));

        return userRepository.save(userInfo);
    }

    // LoginResponse(token, user)

    public String login(LoginUserDto userDto){


        var token = jwtService.generateToken(authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(userDto.getUsername(),userDto.getPassword())).getPrincipal().toString());
        var user = userRepository.findByUsername(userDto.getUsername()).orElseThrow();
        return  token +" | " +  user.getUsername();

    }

}
