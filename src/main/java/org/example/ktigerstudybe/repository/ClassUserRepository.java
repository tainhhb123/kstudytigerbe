package org.example.ktigerstudybe.repository;

import org.example.ktigerstudybe.model.ClassUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassUserRepository extends JpaRepository<ClassUser, Long> {
    List<ClassUser> findByUser_UserId(Long userId);
    List<ClassUser> findByClassEntity_ClassId(Long classId);
}
