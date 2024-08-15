package com.academy.flightsystem.api.filters;

import com.academy.flightsystem.api.security.JwtService;
import com.academy.flightsystem.api.service.UserInfoService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Custom filter responsible for authenticating
 * incoming requests with JWT tokens. Extends OncePerRequestFilter,
 * so it is executed once per request.
 * The filter checks for a JWT token in the "Authorization" header of the HTTP request.
 * If the token is valid - extracts the username, validates the token, and sets
 * the authentication in the SecurityContextHolder, allowing the user to be authenticated
 * for the duration of the request.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserInfoService userInfoService;


    /**
     * The doFilterInternal method is the core method of the filter. It processes
     * the incoming request, checks for a JWT token
     * and attempts to authenticate the user if a valid token is found.
     *
     * @param request     the HTTP request being processed
     * @param response    the HTTP response associated with the request
     * @param filterChain the filter chain for passing the request to the next filter
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Extract the Authorization header from the request
        String header = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // Check if the Authorization header contains a Bearer token
        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
            username = jwtService.extractUsername(token);
        }

        // If a username was extracted and the user is not yet authenticated
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userInfoService.loadUserByUsername(username);

            // Validate the token with the user details
            if (jwtService.isTokenValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                userToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // Set the authentication token in the SecurityContext
                SecurityContextHolder.getContext().setAuthentication(userToken);
            }
            ;

        }

        // pass on and continue with other filters
        filterChain.doFilter(request, response);

    }
}
