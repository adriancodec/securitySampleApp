package com.cc.security.controller;

import com.cc.security.logic.JwtGenerator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("login")
public class LoginEndpoint {

    private final JwtGenerator jwtGenerator;

    public LoginEndpoint(JwtGenerator jwtGenerator) {
        this.jwtGenerator = jwtGenerator;
    }

    @GetMapping
    String loginToObtainJwtToken(Authentication authentication){
        System.out.println("User Logged in, generating and sending back JWT");
        return jwtGenerator.generate(authentication);
    }

}
