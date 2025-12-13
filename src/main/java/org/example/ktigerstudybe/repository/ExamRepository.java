package org.example.ktigerstudybe.repository;

import org.example.ktigerstudybe.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamRepository extends JpaRepository<Exam, Long> {

    // Lấy danh sách exam đang active
    List<Exam> findByIsActiveTrue();
}
