package org.example.usermanagementapplication.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.usermanagementapplication.model.Project;
import org.example.usermanagementapplication.model.User;
import org.example.usermanagementapplication.model.UserStatus;
import org.example.usermanagementapplication.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;

    public List<Project> getUserProjects(User user) {
        return projectRepository.findByUser(user);
    }

    @Transactional
    public Project createProject(User user, String title, String description) {
        if (!user.getStatus().equals(UserStatus.APPROVED)) {
            throw new IllegalStateException("Only approved users can create projects.");
        }

        Project project = new Project();
        project.setUser(user);
        project.setTitle(title);
        project.setDescription(description);
        project.setCreatedAt(LocalDateTime.now());
        return projectRepository.save(project);
    }

    @Transactional
    public Project updateProject(Long projectId, User user, String title, String description) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (!project.getUser().equals(user)) {
            throw new IllegalStateException("You can only update your own projects.");
        }

        project.setTitle(title);
        project.setDescription(description);
        project.setUpdatedAt(LocalDateTime.now());
        return projectRepository.save(project);
    }

    @Transactional
    public void deleteProject(Long projectId, User user) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (!project.getUser().equals(user)) {
            throw new IllegalStateException("You can only delete your own projects.");
        }

        projectRepository.delete(project);
    }
}
