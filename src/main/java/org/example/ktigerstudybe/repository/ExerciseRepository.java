package org.example.ktigerstudybe.repository;

import org.example.ktigerstudybe.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
}
