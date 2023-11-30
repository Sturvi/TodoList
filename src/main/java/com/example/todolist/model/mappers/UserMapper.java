package com.example.todolist.model.mappers;

import com.example.todolist.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {


    public User createNewUser (String username, String password, String name, String surname, String email) {

        return User.builder()
                .username(username)
                .password(password)
                .firstName(name)
                .lastName(surname)
                .email(email)
                .build();
    }
}
