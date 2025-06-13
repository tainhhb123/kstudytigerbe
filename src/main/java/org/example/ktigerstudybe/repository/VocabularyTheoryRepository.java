package org.example.ktigerstudybe.repository;

import org.example.ktigerstudybe.model.VocabularyTheory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VocabularyTheoryRepository extends JpaRepository<VocabularyTheory, Long> {
    @Query("SELECT v FROM VocabularyTheory v WHERE v.lesson.level.levelId = :levelId")
    List<VocabularyTheory> findByLevelId(@Param("levelId") Long levelId);

    List<VocabularyTheory> findByLesson_LessonId(Long lessonId);
}
