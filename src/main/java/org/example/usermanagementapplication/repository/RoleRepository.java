package org.example.usermanagementapplication.repository;

import org.example.usermanagementapplication.model.Role;
import org.example.usermanagementapplication.model.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleType name);
}
