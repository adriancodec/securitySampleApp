package com.cc.security.services;

import com.cc.security.data.User;
import com.cc.security.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User saveUser(final User user) {
        return userRepository.save(user);
    }

    public List<User> saveMoreUsers(final ArrayList<User> listOfUsersToCreate) {
        return userRepository.saveAll(listOfUsersToCreate);
    }
}
