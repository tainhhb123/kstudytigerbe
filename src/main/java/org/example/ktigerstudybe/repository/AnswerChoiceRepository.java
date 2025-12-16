package org.example.ktigerstudybe.repository;


import org.example.ktigerstudybe.model.AnswerChoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerChoiceRepository extends JpaRepository<AnswerChoice, Long> {

    /**
     * Find all choices for a specific question
     */
    @Query("SELECT ac FROM AnswerChoice ac " +
            "WHERE ac.question.questionId = :questionId " +
            "ORDER BY ac.choiceLabel")
    List<AnswerChoice> findByQuestionId(@Param("questionId") Long questionId);

    /**
     * Find the correct answer for a question
     */
    @Query("SELECT ac FROM AnswerChoice ac " +
            "WHERE ac.question.questionId = :questionId " +
            "AND ac.isCorrect = true")
    AnswerChoice findCorrectAnswer(@Param("questionId") Long questionId);
}
