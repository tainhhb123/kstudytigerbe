package org.example.ktigerstudybe.service.sentencerewritingquestion;

import org.example.ktigerstudybe.dto.req.SentenceRewritingQuestionRequest;
import org.example.ktigerstudybe.dto.resp.SentenceRewritingQuestionResponse;
import org.example.ktigerstudybe.model.Exercise;
import org.example.ktigerstudybe.model.SentenceRewritingQuestion;
import org.example.ktigerstudybe.repository.ExerciseRepository;
import org.example.ktigerstudybe.repository.SentenceRewritingQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SentenceRewritingQuestionServiceImpl implements SentenceRewritingQuestionService{

    @Autowired
    private SentenceRewritingQuestionRepository questionRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    private SentenceRewritingQuestionResponse toResponse(SentenceRewritingQuestion entity) {
        SentenceRewritingQuestionResponse resp = new SentenceRewritingQuestionResponse();
        resp.setQuestionId(entity.getQuestionID());
        resp.setExerciseId(entity.getExercise().getExerciseId());
        resp.setOriginalSentence(entity.getOriginalSentence());
        resp.setRewrittenSentence(entity.getRewrittenSentence());
        resp.setLinkMedia(entity.getLinkMedia());
        return resp;
    }

    private SentenceRewritingQuestion toEntity(SentenceRewritingQuestionRequest req) {
        SentenceRewritingQuestion entity = new SentenceRewritingQuestion();
        Exercise exercise = exerciseRepository.findById(req.getExerciseId())
                .orElseThrow(() -> new IllegalArgumentException("Exercise not found: " + req.getExerciseId()));
        entity.setExercise(exercise);
        entity.setOriginalSentence(req.getOriginalSentence());
        entity.setRewrittenSentence(req.getRewrittenSentence());
        entity.setLinkMedia(req.getLinkMedia());
        return entity;
    }

    @Override
    public List<SentenceRewritingQuestionResponse> getAllQuestions() {
        return questionRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public SentenceRewritingQuestionResponse getQuestionById(Long id) {
        SentenceRewritingQuestion entity = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Question not found: " + id));
        return toResponse(entity);
    }

    @Override
    public List<SentenceRewritingQuestionResponse> getQuestionsByExerciseId(Long exerciseId) {
        return questionRepository.findAll().stream()
                .filter(q -> q.getExercise().getExerciseId().equals(exerciseId))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public SentenceRewritingQuestionResponse createQuestion(SentenceRewritingQuestionRequest request) {
        SentenceRewritingQuestion entity = toEntity(request);
        entity = questionRepository.save(entity);
        return toResponse(entity);
    }

    @Override
    public SentenceRewritingQuestionResponse updateQuestion(Long id, SentenceRewritingQuestionRequest request) {
        SentenceRewritingQuestion entity = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Question not found: " + id));
        Exercise exercise = exerciseRepository.findById(request.getExerciseId())
                .orElseThrow(() -> new IllegalArgumentException("Exercise not found: " + request.getExerciseId()));
        entity.setExercise(exercise);
        entity.setOriginalSentence(request.getOriginalSentence());
        entity.setRewrittenSentence(request.getRewrittenSentence());
        entity.setLinkMedia(request.getLinkMedia());
        entity = questionRepository.save(entity);
        return toResponse(entity);
    }

    @Override
    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }
}
