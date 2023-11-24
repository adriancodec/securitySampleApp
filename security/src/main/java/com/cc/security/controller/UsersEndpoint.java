package com.cc.security.controller;

import com.cc.security.data.User;
import com.cc.security.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("users")
public class UsersEndpoint {

    UserService userService;

    public UsersEndpoint(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    List<User> getAllUsers(){
        return userService.getAllUsers();
    }
}
