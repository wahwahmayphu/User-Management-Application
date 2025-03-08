package org.example.usermanagementapplication.repository;

import org.example.usermanagementapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPhone(String phone);
    Optional<User> findByNrc(String nrc);
}
