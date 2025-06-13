package org.example.ktigerstudybe.repository;

import org.example.ktigerstudybe.model.MultipleChoiceQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MultipleChoiceQuestionRepository extends JpaRepository<MultipleChoiceQuestion, Long> {
    List<MultipleChoiceQuestion> findByExercise_ExerciseId(Long exerciseId);
}
