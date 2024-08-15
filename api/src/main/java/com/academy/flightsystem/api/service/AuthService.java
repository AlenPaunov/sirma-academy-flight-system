package com.academy.flightsystem.api.service;

import com.academy.flightsystem.api.model.LoginResponse;
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

    /**
     * Registers a new user in the system by encoding their password and saving
     * their information in the database via the repo.
     */
    public UserInfo register(RegisterUserDto userDto){
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(userDto.getUsername());
        userInfo.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userRepository.save(userInfo);
    }


    /**
     * Authenticates a user based on their username and password, generates a JWT token if the
     * authentication is successful, and returns the token along with the username
     */
    public LoginResponse login(LoginUserDto userDto){
        // Authenticate the user and generate a JWT token using their username
        String token = jwtService.generateToken(authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(userDto.getUsername(),userDto.getPassword())).getPrincipal().toString());
        UserInfo user = userRepository.findByUsername(userDto.getUsername()).orElseThrow();

        return new LoginResponse().setToken(token).setExpiresIn(jwtService.getExpirationTime()).setUsername(user.getUsername());

    }

    // LoginResponse(jwt, user)

}
