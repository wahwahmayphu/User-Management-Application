package org.example.usermanagementapplication.repository;

import org.example.usermanagementapplication.model.Blacklist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlacklistRepository extends JpaRepository<Blacklist, Long> {
    Optional<Blacklist> findByPhoneOrNrc(String phone, String nrc);
}
