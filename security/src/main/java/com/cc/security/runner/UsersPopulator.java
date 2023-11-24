package com.cc.security.runner;

import com.cc.security.data.User;
import com.cc.security.repository.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class UsersPopulator {

    @Bean
    ApplicationRunner poppulateUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            User user = new User(0,"adrian", passwordEncoder.encode("123"), Set.of("USER"));
            userRepository.save(user);
        };
    }
}
