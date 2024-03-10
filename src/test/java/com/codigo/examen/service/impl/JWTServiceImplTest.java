package com.codigo.examen.service.impl;

import com.codigo.examen.service.JWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import static org.mockito.Mockito.*;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class JWTServiceImplTest {
    @Mock
    private Claims claims;

    @InjectMocks
    private JWTService jwtService = new JWTServiceImpl();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void generateToken_Success() {
        UserDetails userDetails = createUserWithRole("username", "password", "ROLE_USER");
        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);
        assertNotEquals(0, token.length());
    }


    @Test
    void validateToken() {
        UserDetails userDetails = createUserWithRole("username", "password", "ROLE_USER");
        JWTServiceImpl jwtService = new JWTServiceImpl();
        String token = jwtService.generateToken(userDetails);

        boolean isValid = jwtService.validateToken(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    void extractUserName() {
        UserDetails userDetails = createUserWithRole("username", "password", "ROLE_USER");
        JWTServiceImpl jwtService = new JWTServiceImpl();
        String token = jwtService.generateToken(userDetails);

        String extractedUsername = jwtService.extractUserName(token);

        assertEquals(userDetails.getUsername(), extractedUsername);
    }

    private UserDetails createUserWithRole(String username, String password, String role) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role));
        return new User(username, password, authorities);
    }
}