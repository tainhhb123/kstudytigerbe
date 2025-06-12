package org.example.ktigerstudybe.repository;

import org.example.ktigerstudybe.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByLevel_LevelId(Long levelId);
}
