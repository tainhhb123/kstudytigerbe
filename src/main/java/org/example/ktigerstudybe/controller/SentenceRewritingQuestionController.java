package org.example.ktigerstudybe.controller;

import org.example.ktigerstudybe.dto.req.SentenceRewritingQuestionRequest;
import org.example.ktigerstudybe.dto.resp.SentenceRewritingQuestionResponse;
import org.example.ktigerstudybe.service.sentencerewritingquestion.SentenceRewritingQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sentence-rewriting-questions")
public class SentenceRewritingQuestionController {

    @Autowired
    private SentenceRewritingQuestionService questionService;

    @GetMapping
    public List<SentenceRewritingQuestionResponse> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SentenceRewritingQuestionResponse> getQuestionById(@PathVariable Long id) {
        try {
            SentenceRewritingQuestionResponse resp = questionService.getQuestionById(id);
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/exercise/{exerciseId}")
    public List<SentenceRewritingQuestionResponse> getQuestionsByExerciseId(@PathVariable Long exerciseId) {
        return questionService.getQuestionsByExerciseId(exerciseId);
    }

    @PostMapping
    public SentenceRewritingQuestionResponse createQuestion(@RequestBody SentenceRewritingQuestionRequest request) {
        return questionService.createQuestion(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SentenceRewritingQuestionResponse> updateQuestion(
            @PathVariable Long id,
            @RequestBody SentenceRewritingQuestionRequest request) {
        try {
            SentenceRewritingQuestionResponse updated = questionService.updateQuestion(id, request);
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
