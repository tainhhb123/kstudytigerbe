package org.example.ktigerstudybe.service.answerChoice;

import org.example.ktigerstudybe.dto.req.AnswerChoiceRequest;
import org.example.ktigerstudybe.dto.resp.AnswerChoiceResponse;

import java.util.List;

public interface AnswerChoiceService {

    List<AnswerChoiceResponse> getChoicesByQuestion(Long questionId);

    AnswerChoiceResponse getChoiceById(Long id);

    AnswerChoiceResponse createChoice(AnswerChoiceRequest request);

    AnswerChoiceResponse updateChoice(Long id, AnswerChoiceRequest request);

    void deleteChoice(Long id);
}
