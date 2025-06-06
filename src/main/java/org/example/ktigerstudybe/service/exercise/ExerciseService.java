package org.example.ktigerstudybe.service.exercise;

import org.example.ktigerstudybe.dto.req.ExerciseRequest;
import org.example.ktigerstudybe.dto.resp.ExerciseResponse;

import java.util.List;

public interface ExerciseService {
    List<ExerciseResponse> getAllExercises();
    ExerciseResponse getExerciseById(Long id);
    ExerciseResponse createExercise(ExerciseRequest request);
    ExerciseResponse updateExercise(Long id, ExerciseRequest request);
    void deleteExercise(Long id);
}
