package org.example.ktigerstudybe.repository;

import org.example.ktigerstudybe.model.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {

    @Query("SELECT up FROM UserProgress up WHERE up.user.userId = :userId")
    UserProgress findByUserId(@Param("userId") Long userId);

    @Query("SELECT up FROM UserProgress up")
    List<UserProgress> findAllProgress();
}
