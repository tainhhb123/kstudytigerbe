package org.example.ktigerstudybe.service.multiplechoicequestion;

import org.example.ktigerstudybe.dto.req.MultipleChoiceQuestionRequest;
import org.example.ktigerstudybe.dto.resp.MultipleChoiceQuestionResponse;
import org.example.ktigerstudybe.model.Exercise;
import org.example.ktigerstudybe.model.MultipleChoiceQuestion;
import org.example.ktigerstudybe.repository.ExerciseRepository;
import org.example.ktigerstudybe.repository.MultipleChoiceQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MultipleChoiceQuestionServiceImpl implements MultipleChoiceQuestionService {

    @Autowired
    private MultipleChoiceQuestionRepository questionRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    // Entity → DTO
    private MultipleChoiceQuestionResponse toResponse(MultipleChoiceQuestion entity) {
        MultipleChoiceQuestionResponse resp = new MultipleChoiceQuestionResponse();
        resp.setQuestionId(entity.getQuestionId());
        resp.setExerciseId(entity.getExercise().getExerciseId());
        resp.setQuestionText(entity.getQuestionText());
        resp.setOptionA(entity.getOptionA());
        resp.setOptionB(entity.getOptionB());
        resp.setOptionC(entity.getOptionC());
        resp.setOptionD(entity.getOptionD());
        resp.setCorrectAnswer(entity.getCorrectAnswer());
        resp.setLinkMedia(entity.getLinkMedia());
        return resp;
    }

    // Request → Entity
    private MultipleChoiceQuestion toEntity(MultipleChoiceQuestionRequest req) {
        MultipleChoiceQuestion entity = new MultipleChoiceQuestion();
        Exercise exercise = exerciseRepository.findById(req.getExerciseId())
                .orElseThrow(() -> new IllegalArgumentException("Exercise not found: " + req.getExerciseId()));
        entity.setExercise(exercise);
        entity.setQuestionText(req.getQuestionText());
        entity.setOptionA(req.getOptionA());
        entity.setOptionB(req.getOptionB());
        entity.setOptionC(req.getOptionC());
        entity.setOptionD(req.getOptionD());
        entity.setCorrectAnswer(req.getCorrectAnswer());
        entity.setLinkMedia(req.getLinkMedia());
        return entity;
    }

    @Override
    public List<MultipleChoiceQuestionResponse> getAllQuestions() {
        return questionRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public MultipleChoiceQuestionResponse getQuestionById(Long id) {
        MultipleChoiceQuestion entity = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Question not found: " + id));
        return toResponse(entity);
    }

    @Override
    public List<MultipleChoiceQuestionResponse> getQuestionsByExerciseId(Long exerciseId) {
        return questionRepository.findAll().stream()
                .filter(q -> q.getExercise().getExerciseId().equals(exerciseId))
                .map(this::toResponse)
                .collect(Collectors.toList());
        // hoặc nếu có custom query: questionRepository.findByExercise_ExerciseId(exerciseId)
    }

    @Override
    public MultipleChoiceQuestionResponse createQuestion(MultipleChoiceQuestionRequest request) {
        MultipleChoiceQuestion entity = toEntity(request);
        entity = questionRepository.save(entity);
        return toResponse(entity);
    }

    @Override
    public MultipleChoiceQuestionResponse updateQuestion(Long id, MultipleChoiceQuestionRequest request) {
        MultipleChoiceQuestion entity = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Question not found: " + id));
        Exercise exercise = exerciseRepository.findById(request.getExerciseId())
                .orElseThrow(() -> new IllegalArgumentException("Exercise not found: " + request.getExerciseId()));
        entity.setExercise(exercise);
        entity.setQuestionText(request.getQuestionText());
        entity.setOptionA(request.getOptionA());
        entity.setOptionB(request.getOptionB());
        entity.setOptionC(request.getOptionC());
        entity.setOptionD(request.getOptionD());
        entity.setCorrectAnswer(request.getCorrectAnswer());
        entity.setLinkMedia(request.getLinkMedia());
        entity = questionRepository.save(entity);
        return toResponse(entity);
    }

    @Override
    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }
}
