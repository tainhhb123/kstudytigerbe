package org.example.ktigerstudybe.service.multiplechoicequestion;

import org.example.ktigerstudybe.dto.req.MultipleChoiceQuestionRequest;
import org.example.ktigerstudybe.dto.resp.MultipleChoiceQuestionResponse;

import java.util.List;

public interface MultipleChoiceQuestionService {
    List<MultipleChoiceQuestionResponse> getAllQuestions();
    MultipleChoiceQuestionResponse getQuestionById(Long id);
    List<MultipleChoiceQuestionResponse> getQuestionsByExerciseId(Long exerciseId);
    MultipleChoiceQuestionResponse createQuestion(MultipleChoiceQuestionRequest request);
    MultipleChoiceQuestionResponse updateQuestion(Long id, MultipleChoiceQuestionRequest request);
    void deleteQuestion(Long id);
}
