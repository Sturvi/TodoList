package com.example.todolist.service;

import com.example.todolist.model.User;
import com.example.todolist.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public void saveNewUser (User user) {
        userRepository.save(user);
    }

    public boolean checkUsername (String username) {
        username = username.toLowerCase();
        return userRepository.existsByUsername(username);
    }

    public User findByUsername(String username) {
       return userRepository.findByUsername(username);
    }
}
