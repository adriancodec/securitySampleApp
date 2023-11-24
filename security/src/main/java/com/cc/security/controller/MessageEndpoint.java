package com.cc.security.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("messages")
public class MessageEndpoint {

    @GetMapping("public")
    String publicEndpoint(){
        System.out.println("SECURITY: Public endpoint executed");
        return "I don't know who you are, but you reached the public endpoint!";
    }

    @GetMapping("private")
    String privateEndpoint(){
        System.out.println("SECURITY: Private endpoint executed, user is authenticated and authorized");
        return "You are authenticated and authorized. You have provided matching credentials (basicAuth or token).";
    }

    @GetMapping("private/hello")
    String privateGreeting(Authentication authentication){
        System.out.println("SECURITY: Private Greeting endpoint executed, user is authenticated and authorized");
        return "Hello, " + authentication.getName();
    }

}
