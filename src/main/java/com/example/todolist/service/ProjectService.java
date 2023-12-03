package com.example.todolist.service;

import com.example.todolist.model.task.Project;
import com.example.todolist.model.user.User;
import com.example.todolist.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;

    public void save(Project project) {
        if (project.getSortOrder() == null) {
            User user = project.getUser();

            if (projectRepository.countByUser(user) == 0) {
                project.setSortOrder(1);
            } else {
                project.setSortOrder(projectRepository.findMaxSortOrderForUser(user) + 1);
            }
        }

        projectRepository.save(project);
    }

    public Optional<Project> getProject(Long id) {
        return projectRepository.findById(id);
    }

    public void updateProject(Project project) {
        projectRepository.save(project);
    }

    @Transactional
    public void updateProjectPosition(Project previousProject, Project project) {
        var projectsList = new ArrayList<>(project.getUser().getProjects());

        project.setSortOrder(previousProject == null ? 1 : previousProject.getSortOrder() + 1);
        Integer currentCount = project.getSortOrder();

        // Сортировка проектов
        projectsList.sort(Comparator.comparing(Project::getSortOrder, Comparator.nullsLast(Integer::compareTo)));

        // Сбор изменений для последующего применения
        Map<Project, Integer> updatedSortOrders = new HashMap<>();
        for (Project pr : projectsList) {
            if (!pr.equals(project)) {
                if (pr.getSortOrder() != null && pr.getSortOrder() >= currentCount) {
                    updatedSortOrders.put(pr, currentCount++);
                }
            }
        }

        // Применение изменений к проектам
        updatedSortOrders.forEach((pr, sortOrder) -> {
            pr.setSortOrder(sortOrder);
            projectRepository.save(pr);
        });
    }
}
