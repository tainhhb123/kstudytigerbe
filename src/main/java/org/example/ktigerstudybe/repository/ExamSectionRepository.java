package org.example.ktigerstudybe.repository;

import org.example.ktigerstudybe.model.ExamSection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamSectionRepository extends JpaRepository<ExamSection, Long> {

    // Lấy section theo exam (theo thứ tự)
    List<ExamSection> findByExam_ExamIdOrderBySectionOrderAsc(Long examId);
}
