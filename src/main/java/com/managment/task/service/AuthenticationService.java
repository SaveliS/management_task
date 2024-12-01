package com.managment.task.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.managment.task.config.security.CustomEmployeeDetailsService;
import com.managment.task.model.dto.SignInRequest;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthenticationService {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CustomEmployeeDetailsService customEmployeeDetailsService;

    @Autowired
    public AuthenticationService(JwtService jwtService, AuthenticationManager authenticationManager,
            CustomEmployeeDetailsService customEmployeeDetailsService) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.customEmployeeDetailsService = customEmployeeDetailsService;
    }

    /**
     * Аутентификация пользователя через
     * Cookies
     * 
     * @param request данные пользователя
     * @return токен
     */
    public void signInCookie(SignInRequest request, HttpServletResponse response) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getLogin(),
                request.getPassword()));

        UserDetails userDetails = customEmployeeDetailsService
                .loadUserByUsername(request.getLogin());

        String jwtToken = jwtService.generateToken(userDetails);

        Cookie jwtCookie = new Cookie("jwtToken", jwtToken);
        jwtCookie.setHttpOnly(true); // Защита от доступа через JavaScript
        jwtCookie.setSecure(false); // Только для HTTPS
        jwtCookie.setPath("/"); // Доступно для всех путей
        jwtCookie.setMaxAge(60 * 60 * 1); // Срок действия (например, 1 час)

        response.addCookie(jwtCookie);
    }
}
