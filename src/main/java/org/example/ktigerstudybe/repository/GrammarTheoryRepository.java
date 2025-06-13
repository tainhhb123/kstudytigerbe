// === GrammarTheoryRepository.java ===
package org.example.ktigerstudybe.repository;

import org.example.ktigerstudybe.model.GrammarTheory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GrammarTheoryRepository extends JpaRepository<GrammarTheory, Long> {
    @Query("SELECT g FROM GrammarTheory g WHERE g.lesson.level.levelId = :levelId")
    List<GrammarTheory> findByLevelId(@Param("levelId") Long levelId);

    List<GrammarTheory> findByLesson_LessonId(Long lessonId);
}
