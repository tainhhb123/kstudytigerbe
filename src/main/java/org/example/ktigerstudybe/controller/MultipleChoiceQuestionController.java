package org.example.ktigerstudybe.controller;

import org.example.ktigerstudybe.dto.req.MultipleChoiceQuestionRequest;
import org.example.ktigerstudybe.dto.resp.MultipleChoiceQuestionResponse;
import org.example.ktigerstudybe.service.multiplechoicequestion.MultipleChoiceQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/multiple-choice-questions")
public class MultipleChoiceQuestionController {

    @Autowired
    private MultipleChoiceQuestionService questionService;

    @GetMapping
    public List<MultipleChoiceQuestionResponse> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MultipleChoiceQuestionResponse> getQuestionById(@PathVariable Long id) {
        try {
            MultipleChoiceQuestionResponse resp = questionService.getQuestionById(id);
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/exercise/{exerciseId}")
    public List<MultipleChoiceQuestionResponse> getQuestionsByExerciseId(@PathVariable Long exerciseId) {
        return questionService.getQuestionsByExerciseId(exerciseId);
    }

    @PostMapping
    public MultipleChoiceQuestionResponse createQuestion(@RequestBody MultipleChoiceQuestionRequest request) {
        return questionService.createQuestion(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MultipleChoiceQuestionResponse> updateQuestion(
            @PathVariable Long id,
            @RequestBody MultipleChoiceQuestionRequest request) {
        try {
            MultipleChoiceQuestionResponse updated = questionService.updateQuestion(id, request);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }
}
