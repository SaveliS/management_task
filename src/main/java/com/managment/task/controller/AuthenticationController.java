package com.managment.task.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.managment.task.model.dto.SignInRequest;
import com.managment.task.service.AuthenticationService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    @GetMapping("/login")
    public String getLoginPage(Model model){
        model.addAttribute("signIn", new SignInRequest());
        return "security/login";
    }

    @PostMapping("/login")
    public String signIn(@ModelAttribute("signIn") SignInRequest signInRequest, HttpServletResponse response){
        authenticationService.signInCookie(signInRequest, response);
        return "redirect:/";
    }
}
