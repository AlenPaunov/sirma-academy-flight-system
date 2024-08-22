package com.academy.flightsystem.api.service;

import com.academy.flightsystem.api.model.LoginResponse;
import com.academy.flightsystem.api.model.Role;
import com.academy.flightsystem.api.model.UserInfo;
import com.academy.flightsystem.api.model.dto.LoginUserDto;
import com.academy.flightsystem.api.model.dto.RegisterUserDto;
import com.academy.flightsystem.api.repository.RoleRepository;
import com.academy.flightsystem.api.repository.UserRepository;
import com.academy.flightsystem.api.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private JwtService jwtService;

    /**
     * Registers a new user in the system by encoding their password and saving
     * their information in the database via the repo.
     */
    public UserInfo register(RegisterUserDto userDto){
        logger.info("hit register service: {}", userDto);
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(userDto.getUsername());
        userInfo.setPassword(passwordEncoder.encode(userDto.getPassword()));
        Set<Role> roles = Set.of(roleRepository.findByName("ROLE_USER").orElseThrow());
        userInfo.setRoles(roles);
        return userRepository.save(userInfo);
    }


    /**
     * Authenticates a user based on their username and password, generates a JWT token if the
     * authentication is successful, and returns the token along with the username
     */
    public LoginResponse login(LoginUserDto userDto){
        try {
            logger.info("hit login service: {}", userDto);
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword())
            );


            logger.info("Principle: {}", authentication.getPrincipal());
            // If authentication successful, generate JWT
            String token = jwtService.generateToken(authentication.getName());

            // Retrieve the user
            UserInfo user = userRepository.findByUsername(userDto.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            List<String> roles = user.getRoles().stream()
                    .map(Role::getName)
                    .toList();


            // Return response
            return new LoginResponse()
                    .setToken(token)
                    .setExpiresIn(jwtService.getExpirationTime())
                    .setUsername(user.getUsername())
                    .setRoles(roles);

        } catch (UsernameNotFoundException | BadCredentialsException e) {
            logger.info("error login: {}", e);
            throw new BadCredentialsException("Invalid username or password");

        } catch (Exception e) {
            logger.info("exception while login: {}", e);
            throw new RuntimeException("An unexpected error occurred during login", e);
        }
    }

}
