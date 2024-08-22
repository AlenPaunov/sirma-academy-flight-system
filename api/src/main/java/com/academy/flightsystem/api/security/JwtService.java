package com.academy.flightsystem.api.security;

import com.academy.flightsystem.api.service.AuthService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JwtService is responsible for generating, validating, and extracting information
 * from JSON Web Tokens (JWTs). It handles core operations lie creating, extracting claims, and validating tokens against user details.
 */
@Service
public class JwtService {
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);
    @Value("${jwt.secret}")
    private String key;
    @Value("${jwt.expiration}")
    private Long expiration;


    public String generateToken(String username){
        logger.info("generate Token with Username: {}", username);
        var token = buildToken(username);
        logger.info("generated token: {}", token);
        return token;
    };

    // check if token is generated valid
    public String buildToken(String username ){
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + getExpirationTime()))
                .signWith(getSigningKey())
                .compact();
    }

    private SecretKey getSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode(key);
        return  Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token){
        String username = extractClaim(token, Claims::getSubject);
        logger.info("extracted username: {}", username);
        return username;
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        Claims claims = getAllClaims(token);
        logger.info("claims: {}", claims);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public long getExpirationTime(){
        return expiration;
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        // TODO check if is expired
        return username.equals(userDetails.getUsername());
    }


}
