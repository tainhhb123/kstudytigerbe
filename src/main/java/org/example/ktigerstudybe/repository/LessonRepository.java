package org.example.ktigerstudybe.repository;

import org.example.ktigerstudybe.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
}
