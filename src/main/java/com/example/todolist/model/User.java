package com.example.todolist.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users")
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
public class User extends  AbstractEntity{

    @NonNull
    private String username;

    @NonNull
    private String password;

    private String firstName;

    private String lastName;

    @NonNull
    private String email;

    @Enumerated(EnumType.STRING)
    @NonNull
    private RoleEnum role = RoleEnum.USER;
}
