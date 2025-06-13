package org.example.ktigerstudybe.repository;

import org.example.ktigerstudybe.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Search users by name, email or username
    Page<User> findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrUserNameContainingIgnoreCase(
            String fullName, String email, String userName, Pageable pageable);

    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);

    long countByUserStatus(int status);

    long countByJoinDateAfter(java.time.LocalDate date);
}
