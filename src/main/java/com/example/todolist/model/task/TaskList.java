package com.example.todolist.model.task;

import com.example.todolist.model.AbstractEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tasklists")
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
public class TaskList extends AbstractEntity implements HasParentEntity,ParentEntity{

    private String name;

    @ManyToOne
    private Project project;

    @OneToMany(mappedBy = "taskList", cascade = CascadeType.ALL)
    private List<Task> tasks;

    private Integer sortOrder;

    /**
     * Retrieves the parent entity of this object.
     *
     * @return The parent entity.
     */
    @Override
    public ParentEntity getParent() {
        return project;
    }

    /**
     * Determines if the object has a parent.
     *
     * @return true if the object has a parent, false otherwise.
     */
    @Override
    public boolean hasParent() {
        return project != null;
    }
}