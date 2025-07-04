package org.example.ktigerstudybe.repository;

import org.example.ktigerstudybe.model.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {
    Optional<UserProgress> findByUser_UserIdAndLesson_LessonId(Long userId, Long lessonId);
    boolean existsByUser_UserIdAndLesson_LessonId(Long userId, Long lessonId);

    List<UserProgress> findByUser_UserId(Long userUserId);

}
