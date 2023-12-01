package com.example.todolist.model.user;

import com.example.todolist.model.AbstractEntity;
import com.example.todolist.model.task.Project;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users")
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
public class User extends AbstractEntity {

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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Project> projects;
}
