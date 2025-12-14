package org.example.ktigerstudybe.repository;

import org.example.ktigerstudybe.model.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {

    List<UserAnswer> findByAttempt_AttemptId(Long attemptId);

    Optional<UserAnswer> findByAttempt_AttemptIdAndQuestion_QuestionId(
            Long attemptId,
            Long questionId
    );
}
