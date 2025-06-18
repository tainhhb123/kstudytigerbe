package org.example.ktigerstudybe.repository;


import org.example.ktigerstudybe.model.UserXP;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserXPRepository extends JpaRepository<UserXP, Long> {
    Optional<UserXP> findByUser_UserId(Long userId);
}