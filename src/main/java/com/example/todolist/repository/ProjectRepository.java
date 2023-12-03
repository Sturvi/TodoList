package com.example.todolist.repository;

import com.example.todolist.model.task.Project;
import com.example.todolist.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT MAX(p.sortOrder) FROM Project p WHERE p.user = :user")
    Integer findMaxSortOrderForUser(User user);

    Long countByUser (User user);

    List<Project> findByUser (User user);
}
