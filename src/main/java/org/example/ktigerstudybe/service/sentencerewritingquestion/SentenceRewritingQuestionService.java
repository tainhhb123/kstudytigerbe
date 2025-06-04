package org.example.ktigerstudybe.service.sentencerewritingquestion;

import org.example.ktigerstudybe.dto.req.SentenceRewritingQuestionRequest;
import org.example.ktigerstudybe.dto.resp.SentenceRewritingQuestionResponse;
import org.example.ktigerstudybe.model.SentenceRewritingQuestion;

import java.util.List;

public interface SentenceRewritingQuestionService {
    List<SentenceRewritingQuestionResponse> getAllQuestions();
    SentenceRewritingQuestionResponse getQuestionById(Long id);
    List<SentenceRewritingQuestionResponse> getQuestionsByExerciseId(Long exerciseId);
    SentenceRewritingQuestionResponse createQuestion(SentenceRewritingQuestionRequest request);
    SentenceRewritingQuestionResponse updateQuestion(Long id, SentenceRewritingQuestionRequest request);
    void deleteQuestion(Long id);


}
