package org.example.ktigerstudybe.service.question;

import org.example.ktigerstudybe.dto.req.QuestionRequest;
import org.example.ktigerstudybe.dto.resp.AnswerChoiceResponse;
import org.example.ktigerstudybe.dto.resp.QuestionResponse;
import org.example.ktigerstudybe.model.AnswerChoice;
import org.example.ktigerstudybe.model.ExamSection;
import org.example.ktigerstudybe.model.Question;
import org.example.ktigerstudybe.repository.AnswerChoiceRepository;
import org.example.ktigerstudybe.repository.ExamSectionRepository;
import org.example.ktigerstudybe.repository.QuestionRepository;
import org.example.ktigerstudybe.repository.UserAnswerRepository;  // ← THÊM IMPORT
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ExamSectionRepository examSectionRepository;

    @Autowired
    private AnswerChoiceRepository answerChoiceRepository;

    @Autowired
    private UserAnswerRepository userAnswerRepository;  // ← THÊM INJECT

    // ===== Mapper for AnswerChoice =====
    private AnswerChoiceResponse toAnswerChoiceResponse(AnswerChoice ac) {
        AnswerChoiceResponse resp = new AnswerChoiceResponse();
        resp.setChoiceId(ac.getChoiceId());
        resp.setQuestionId(ac.getQuestion().getQuestionId());
        resp.setChoiceLabel(ac.getChoiceLabel());
        resp.setChoiceText(ac.getChoiceText());
        resp.setIsCorrect(ac.getIsCorrect());
        return resp;
    }

    // ===== Mapper for Question =====
    private QuestionResponse toResponse(Question q) {
        QuestionResponse resp = new QuestionResponse();

        resp.setQuestionId(q.getQuestionId());
        resp.setSectionId(q.getSection().getSectionId());
        resp.setGroupId(q.getGroupId());
        resp.setQuestionNumber(q.getQuestionNumber());
        resp.setQuestionType(q.getQuestionType());
        resp.setQuestionText(q.getQuestionText());
        resp.setPassageText(q.getPassageText());
        resp.setAudioUrl(q.getAudioUrl());
        resp.setImageUrl(q.getImageUrl());
        resp.setPoints(q.getPoints());
        resp.setCorrectAnswer(q.getCorrectAnswer());

        // Map choices
        List<AnswerChoice> choices = answerChoiceRepository.findByQuestionId(q.getQuestionId());
        resp.setChoices(
                choices.stream()
                        .map(this::toAnswerChoiceResponse)
                        .collect(Collectors.toList())
        );

        return resp;
    }

    @Override
    public List<QuestionResponse> getQuestionsBySection(Long sectionId) {
        return questionRepository
                .findBySection_SectionIdOrderByQuestionNumberAsc(sectionId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public QuestionResponse getQuestionById(Long id) {
        Question q = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Question not found"));
        return toResponse(q);
    }

    @Override
    public QuestionResponse createQuestion(QuestionRequest request) {
        ExamSection section = examSectionRepository.findById(request.getSectionId())
                .orElseThrow(() -> new IllegalArgumentException("ExamSection not found"));

        Question q = new Question();
        q.setSection(section);
        q.setGroupId(request.getGroupId());
        q.setQuestionNumber(request.getQuestionNumber());
        q.setQuestionType(request.getQuestionType());
        q.setQuestionText(request.getQuestionText());
        q.setPassageText(request.getPassageText());
        q.setAudioUrl(request.getAudioUrl());
        q.setImageUrl(request.getImageUrl());
        q.setCorrectAnswer(request.getCorrectAnswer());
        q.setPoints(
                request.getPoints() != null ? request.getPoints() : q.getPoints()
        );

        q = questionRepository.save(q);
        return toResponse(q);
    }

    @Override
    public QuestionResponse updateQuestion(Long id, QuestionRequest request) {
        Question q = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Question not found"));

        q.setGroupId(request.getGroupId());
        q.setQuestionNumber(request.getQuestionNumber());
        q.setQuestionType(request.getQuestionType());
        q.setQuestionText(request.getQuestionText());
        q.setPassageText(request.getPassageText());
        q.setAudioUrl(request.getAudioUrl());
        q.setImageUrl(request.getImageUrl());
        q.setCorrectAnswer(request.getCorrectAnswer());
        q.setPoints(request.getPoints());

        q = questionRepository.save(q);
        return toResponse(q);
    }

    @Override
    public void deleteQuestion(Long id) {
        // 1. Lấy tất cả answer_choice của question này
        List<AnswerChoice> choices = answerChoiceRepository.findByQuestionId(id);

        // 2. Xóa tất cả user_answer tham chiếu đến các choice này
        for (AnswerChoice choice : choices) {
            userAnswerRepository.deleteByChoiceChoiceId(choice.getChoiceId());
        }

        // 3. Xóa user_answer theo questionId (cho SHORT/ESSAY không có choice)
        userAnswerRepository.deleteByQuestionQuestionId(id);

        // 4. Xóa question (cascade sẽ tự động xóa answer_choices)
        questionRepository.deleteById(id);
    }
}