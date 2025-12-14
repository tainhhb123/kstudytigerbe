package org.example.ktigerstudybe.repository;

import org.example.ktigerstudybe.model.ExamAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamAttemptRepository extends JpaRepository<ExamAttempt, Long> {

    List<ExamAttempt> findByUser_UserId(Long userId);

    List<ExamAttempt> findByExam_ExamId(Long examId);
}
