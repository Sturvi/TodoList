package com.example.todolist.model.task;

/**
 * A contract defining an entity that has a parent entity.
 */
public interface HasParentEntity {

    ParentEntity getParent();

    boolean hasParent();
}
