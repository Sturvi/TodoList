package com.example.todolist.model.task;

import com.example.todolist.model.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tasks_comments")
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
public class Comment extends AbstractEntity implements HasParentEntity{

    private String text;

    @ManyToOne
    private Task task;

    @Override
    public ParentEntity getParent() {
        return task;
    }

    @Override
    public boolean hasParent() {
        return task != null;
    }
}
