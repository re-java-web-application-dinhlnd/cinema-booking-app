package com.re.cinemabookingapp.repository;

import com.re.cinemabookingapp.entity.User;
import com.re.cinemabookingapp.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByUserProfileEmail(String email);
    long countByRole(UserRole role);
}
