package com.academy.flightsystem.api.service;

import com.academy.flightsystem.api.model.UserInfo;
import com.academy.flightsystem.api.model.dto.LoginUserDto;
import com.academy.flightsystem.api.model.dto.RegisterUserDto;
import com.academy.flightsystem.api.repository.UserRepository;
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

    public UserInfo register(RegisterUserDto userDto){
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(userDto.getUsername());
        userInfo.setPassword(passwordEncoder.encode(userDto.getPassword()));

        return userRepository.save(userInfo);
    }

    public Optional<UserInfo> login(LoginUserDto userDto){
        authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(userDto.getUsername(),userDto.getPassword()));
        return userRepository.findByUsername(userDto.getUsername());
    }

}
