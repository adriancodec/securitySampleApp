package com.cc.security.runner;

import com.cc.security.data.User;
import com.cc.security.services.UserService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Set;

@Configuration
public class UsersPopulator {

    @Bean
    ApplicationRunner populateUsers(UserService userService, PasswordEncoder passwordEncoder) {
        return args -> {
            ArrayList<User> listOfUsersToCreate = new ArrayList<>();
            listOfUsersToCreate.add(new User("adrian", passwordEncoder.encode("123"), "Adrian Developer", Set.of("USER"), Set.of("USER"))); //TODO NOT ROLE_USER
            listOfUsersToCreate.add(new User("admin", passwordEncoder.encode("456"), "App Admin", Set.of("ADMIN"), Set.of("ADMIN")));//TODO NOT ROLE_ADMIN
            userService.saveMoreUsers(listOfUsersToCreate);
        };
    }
}
