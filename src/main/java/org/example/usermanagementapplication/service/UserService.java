package org.example.usermanagementapplication.service;

import lombok.RequiredArgsConstructor;
import org.example.usermanagementapplication.model.*;
import org.example.usermanagementapplication.repository.BlacklistRepository;
import org.example.usermanagementapplication.repository.RoleRepository;
import org.example.usermanagementapplication.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BlacklistRepository blacklistRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public Optional<User> findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    public Optional<User> findByNrc(String nrc) {
        return userRepository.findByNrc(nrc);
    }

    @Transactional
    public User registerUser(String phone, String nrc, String password) {
        if (blacklistRepository.findByPhoneOrNrc(phone, nrc).isPresent()) {
            throw new IllegalStateException("User is blacklisted and cannot register.");
        }

        User user = new User();
        user.setPhone(phone);
        user.setNrc(nrc);
        user.setPassword(passwordEncoder.encode(password));
        user.setStatus(UserStatus.PENDING);

        Role userRole = roleRepository.findByName(RoleType.REGISTERED_USER)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.setRoles(Set.of(userRole));
        return userRepository.save(user);
    }

    @Transactional
    public void approveUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setStatus(UserStatus.APPROVED);
        userRepository.save(user);
    }

    @Transactional
    public void rejectUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setStatus(UserStatus.REJECTED);
        userRepository.save(user);
    }

    @Transactional
    public void blockUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setStatus(UserStatus.BLOCKED);
        userRepository.save(user);
    }

    @Transactional
    public void blacklistUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Blacklist blacklist = new Blacklist();
        blacklist.setPhone(user.getPhone());
        blacklist.setNrc(user.getNrc());
        blacklistRepository.save(blacklist);

        user.setStatus(UserStatus.BLOCKED);
        userRepository.save(user);
    }
}
