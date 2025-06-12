package org.example.ktigerstudybe.repository;

import org.example.ktigerstudybe.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

<<<<<<< HEAD
import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByLevel_LevelId(Long levelId);
=======
public interface LessonRepository extends JpaRepository<Lesson, Long> {
>>>>>>> f7cadc7 (cap nhat api user sign up in)
}
