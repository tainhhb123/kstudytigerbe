package org.example.ktigerstudybe.repository;

import org.example.ktigerstudybe.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Search users by name, email or username
    Page<User> findByRole(String role, Pageable pageable);

    Page<User> findByRoleAndFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String role, String fullName, String email, Pageable pageable);

    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);

    long countByUserStatus(int status);

    long countByJoinDateAfter(java.time.LocalDate date);

    // UserRepository - thêm method này
    @Query("SELECT u FROM User u WHERE u.userStatus = 1 AND u.email IS NOT NULL")
    List<User> findAllActiveUsers();

    Page<User> findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String fullName, String email, Pageable pageable);

}
