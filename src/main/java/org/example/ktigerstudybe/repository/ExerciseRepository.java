package org.example.ktigerstudybe.repository;

import org.example.ktigerstudybe.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    List<Exercise> findByLesson_LessonId(Long lessonId);
}
