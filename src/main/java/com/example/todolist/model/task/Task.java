package com.example.todolist.model.task;

import com.example.todolist.model.AbstractEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tasks")
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
public class Task extends AbstractEntity implements HasParentEntity, ParentEntity {

    private String name;
    private String description;

    @OneToMany(mappedBy = "parentTask", cascade = CascadeType.ALL)
    private List<Task> subTasks;

    @ManyToOne
    private Task parentTask;

    @ManyToOne
    private TaskList taskList;

    @ManyToOne
    private Project project;

    @Enumerated(EnumType.STRING)
    private PriorityEnum priority;

    private LocalDateTime deadline;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<Comment> comments;

    private Integer sortOrder;

    /**
     * This method returns the parent entity of the current instance.
     *
     * @return the parent entity of the current instance. It can be either parent task, project, or task list.
     */
    @Override
    public ParentEntity getParent() {
        return parentTask != null ? parentTask
                : project != null ? project
                : taskList;
    }

    /**
     * Checks if the current task has a parent.
     * @return true if the task has a parent, false otherwise.
     */
    @Override
    public boolean hasParent() {
        return parentTask != null || project != null || taskList != null;
    }
}
