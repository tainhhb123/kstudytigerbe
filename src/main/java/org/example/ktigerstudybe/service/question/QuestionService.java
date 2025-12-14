package org.example.ktigerstudybe.service.question;

import org.example.ktigerstudybe.dto.req.QuestionRequest;
import org.example.ktigerstudybe.dto.resp.QuestionResponse;

import java.util.List;

public interface QuestionService {

    List<QuestionResponse> getQuestionsBySection(Long sectionId);

    QuestionResponse getQuestionById(Long id);

    QuestionResponse createQuestion(QuestionRequest request);

    QuestionResponse updateQuestion(Long id, QuestionRequest request);

    void deleteQuestion(Long id);
}
