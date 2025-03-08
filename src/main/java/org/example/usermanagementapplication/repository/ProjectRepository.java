package org.example.usermanagementapplication.repository;

import org.example.usermanagementapplication.model.Project;
import org.example.usermanagementapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByUser(User user);
}
