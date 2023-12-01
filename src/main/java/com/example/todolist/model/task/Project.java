package com.example.todolist.model.task;

import com.example.todolist.model.AbstractEntity;
import com.example.todolist.model.user.User;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "projects")
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
public class Project extends AbstractEntity implements ParentEntity{

    private String name;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Task> tasks;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<TaskList> taskLists;

    private Integer sortOrder;
}
