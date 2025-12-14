package org.example.ktigerstudybe.repository;

import org.example.ktigerstudybe.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    // Lấy câu hỏi theo section, đúng thứ tự
    List<Question> findBySection_SectionIdOrderByQuestionNumberAsc(Long sectionId);

    // Lấy câu hỏi theo group
    List<Question> findByGroupId(Long groupId);
}
