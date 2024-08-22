package com.academy.flightsystem.client.service;

import com.academy.flightsystem.client.model.LoginResponse;
import com.academy.flightsystem.client.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private String jwtToken;

    @Value("${backend.api.url}")
    private String backendApiUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HttpSession session;

    public void register(User user) {
        try {
            logger.info("Registering user: {}", user.getUsername());
            String response = restTemplate.postForObject(backendApiUrl + "/auth/register", user, String.class);
            logger.info("User registered successfully: {}", user.getUsername());
            logger.info("Registration response: {}", response);

        } catch (Exception e) {
            logger.info("Error: {}", e.getMessage());
            throw e;
        }

    }

    public void login(User user) throws Exception {
        try {
            logger.info("Logging in user: {}", user.getUsername());
            ResponseEntity<LoginResponse> response = restTemplate.postForEntity(backendApiUrl + "/auth/login", user, LoginResponse.class);

            this.jwtToken = response.getBody().getToken();
            session.setAttribute("jwtToken", this.jwtToken);
            List<GrantedAuthority> authorities = response.getBody().getRoles().stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(), "", authorities);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            // Set authentication into the security context
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(securityContext);
            // Ensure the session contains the security context
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);

            logger.info("User logged in successfully: {}", user.getUsername());
        } catch (HttpClientErrorException.Unauthorized e) {
            logger.error("Login failed for user {}: Unauthorized", user.getUsername());
            throw new RuntimeException("Invalid username or password");
        } catch (Exception e) {
            logger.error("Unexpected error during login for user {}: {}", user.getUsername(), e.getMessage());
            throw e;
        }

    }

    public void testAdminJWT() {
        HttpHeaders headers = createHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(backendApiUrl + "/auth/admin", HttpMethod.GET, entity, String.class);
        logger.info(response.getBody());

    }

    public void testUserJWT() {
        HttpHeaders headers = createHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(backendApiUrl + "/auth/user", HttpMethod.GET, entity, String.class);
        logger.info(response.getBody());

    }


    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        if (jwtToken != null && !jwtToken.isEmpty()) {
            headers.set("Authorization", "Bearer " + jwtToken);
        }
        return headers;
    }

    public <T> T getProtectedResource(String endpoint, Class<T> responseType) {
        HttpHeaders headers = createHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(backendApiUrl + endpoint, HttpMethod.GET, entity, responseType).getBody();
    }

    public <T> T postProtectedResource(String endpoint, Object request, Class<T> responseType) {
        HttpHeaders headers = createHeaders();
        HttpEntity<Object> entity = new HttpEntity<>(request, headers);
        return restTemplate.exchange(backendApiUrl + endpoint, HttpMethod.POST, entity, responseType).getBody();
    }
}
