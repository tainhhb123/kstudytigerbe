package org.example.ktigerstudybe.service.userAnswer;

import org.example.ktigerstudybe.dto.req.UserAnswerRequest;
import org.example.ktigerstudybe.dto.resp.UserAnswerResponse;
import org.example.ktigerstudybe.model.*;
import org.example.ktigerstudybe.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserAnswerServiceImpl implements UserAnswerService {

    @Autowired
    private UserAnswerRepository userAnswerRepository;

    @Autowired
    private ExamAttemptRepository examAttemptRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerChoiceRepository answerChoiceRepository;

    // ===== Mapper =====
    private UserAnswerResponse toResponse(UserAnswer ua) {
        UserAnswerResponse resp = new UserAnswerResponse();
        resp.setUserAnswerId(ua.getUserAnswerId());
        resp.setAttemptId(ua.getAttempt().getAttemptId());
        resp.setQuestionId(ua.getQuestion().getQuestionId());
        resp.setChoiceId(
                ua.getChoice() != null ? ua.getChoice().getChoiceId() : null
        );
        resp.setAnswerText(ua.getAnswerText());
        resp.setScore(ua.getScore());
        return resp;
    }

    @Override
    public UserAnswerResponse saveUserAnswer(UserAnswerRequest request) {

        ExamAttempt attempt = examAttemptRepository.findById(request.getAttemptId())
                .orElseThrow(() -> new IllegalArgumentException("Attempt not found"));

        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new IllegalArgumentException("Question not found"));

        // ===== Check nếu đã trả lời câu này =====
        UserAnswer ua = userAnswerRepository
                .findByAttempt_AttemptIdAndQuestion_QuestionId(
                        request.getAttemptId(),
                        request.getQuestionId()
                )
                .orElse(new UserAnswer());

        ua.setAttempt(attempt);
        ua.setQuestion(question);

        // ===== MCQ =====
        if (request.getChoiceId() != null) {
            AnswerChoice choice = answerChoiceRepository.findById(request.getChoiceId())
                    .orElseThrow(() -> new IllegalArgumentException("Choice not found"));

            ua.setChoice(choice);
            ua.setAnswerText(null);

            // ===== Chấm điểm tự động =====
            if (Boolean.TRUE.equals(choice.getIsCorrect())) {
                ua.setScore(question.getPoints());
            } else {
                ua.setScore(BigDecimal.ZERO);
            }
        }
        // ===== SHORT / ESSAY =====
        else {
            ua.setChoice(null);
            ua.setAnswerText(request.getAnswerText());

            // SHORT có thể auto (sau), ESSAY = 0
            ua.setScore(BigDecimal.ZERO);
        }

        ua = userAnswerRepository.save(ua);
        return toResponse(ua);
    }

    @Override
    public List<UserAnswerResponse> getAnswersByAttempt(Long attemptId) {
        return userAnswerRepository.findByAttempt_AttemptId(attemptId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
