package org.example.ktigerstudybe.service.answerChoice;

import org.example.ktigerstudybe.dto.req.AnswerChoiceRequest;
import org.example.ktigerstudybe.dto.resp.AnswerChoiceResponse;
import org.example.ktigerstudybe.model.AnswerChoice;
import org.example.ktigerstudybe.model.Question;
import org.example.ktigerstudybe.repository.AnswerChoiceRepository;
import org.example.ktigerstudybe.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnswerChoiceServiceImpl implements AnswerChoiceService {

    @Autowired
    private AnswerChoiceRepository answerChoiceRepository;

    @Autowired
    private QuestionRepository questionRepository;

    // ===== Mapper =====
    private AnswerChoiceResponse toResponse(AnswerChoice c) {
        AnswerChoiceResponse resp = new AnswerChoiceResponse();
        resp.setChoiceId(c.getChoiceId());
        resp.setQuestionId(c.getQuestion().getQuestionId());
        resp.setChoiceLabel(c.getChoiceLabel());
        resp.setChoiceText(c.getChoiceText());
        return resp;
    }

    @Override
    public List<AnswerChoiceResponse> getChoicesByQuestion(Long questionId) {
        return answerChoiceRepository.findByQuestionId(questionId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AnswerChoiceResponse getChoiceById(Long id) {
        AnswerChoice c = answerChoiceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("AnswerChoice not found"));
        return toResponse(c);
    }

    @Override
    public AnswerChoiceResponse createChoice(AnswerChoiceRequest request) {

        Question q = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new IllegalArgumentException("Question not found"));

        AnswerChoice c = new AnswerChoice();
        c.setQuestion(q);
        c.setChoiceLabel(request.getChoiceLabel());
        c.setChoiceText(request.getChoiceText());
        c.setIsCorrect(
                request.getIsCorrect() != null ? request.getIsCorrect() : false
        );

        c = answerChoiceRepository.save(c);
        return toResponse(c);
    }

    @Override
    public AnswerChoiceResponse updateChoice(Long id, AnswerChoiceRequest request) {

        AnswerChoice c = answerChoiceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("AnswerChoice not found"));

        c.setChoiceLabel(request.getChoiceLabel());
        c.setChoiceText(request.getChoiceText());
        c.setIsCorrect(request.getIsCorrect());

        c = answerChoiceRepository.save(c);
        return toResponse(c);
    }

    @Override
    public void deleteChoice(Long id) {
        answerChoiceRepository.deleteById(id);
    }
}
