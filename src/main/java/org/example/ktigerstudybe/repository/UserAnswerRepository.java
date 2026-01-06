package org.example.ktigerstudybe.repository;

import org.example.ktigerstudybe.model.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {

    List<UserAnswer> findByAttempt_AttemptId(Long attemptId);

    Optional<UserAnswer> findByAttempt_AttemptIdAndQuestion_QuestionId(
            Long attemptId,
            Long questionId
    );

    @Modifying
    @Query("DELETE FROM UserAnswer ua WHERE ua.choice.choiceId = :choiceId")
    void deleteByChoiceChoiceId(@Param("choiceId") Long choiceId);

    @Modifying
    @Query("DELETE FROM UserAnswer ua WHERE ua.question.questionId = :questionId")
    void deleteByQuestionQuestionId(@Param("questionId") Long questionId);
}
