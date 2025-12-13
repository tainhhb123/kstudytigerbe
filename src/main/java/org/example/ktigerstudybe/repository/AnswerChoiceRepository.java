package org.example.ktigerstudybe.repository;

import org.example.ktigerstudybe.model.AnswerChoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerChoiceRepository extends JpaRepository<AnswerChoice, Long> {

    // Lấy các lựa chọn của 1 câu hỏi
    List<AnswerChoice> findByQuestion_QuestionId(Long questionId);
}
