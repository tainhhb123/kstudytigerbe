package org.example.ktigerstudybe.repository;

import org.example.ktigerstudybe.model.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassRepository extends JpaRepository<ClassEntity, Long> {
    List<ClassEntity> findByUser_UserId(Long userId);
}
