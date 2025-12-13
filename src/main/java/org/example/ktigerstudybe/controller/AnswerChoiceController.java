package org.example.ktigerstudybe.controller;

import org.example.ktigerstudybe.dto.req.AnswerChoiceRequest;
import org.example.ktigerstudybe.dto.resp.AnswerChoiceResponse;
import org.example.ktigerstudybe.service.answerChoice.AnswerChoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/answer-choices")
public class AnswerChoiceController {

    @Autowired
    private AnswerChoiceService answerChoiceService;

    // Lấy đáp án theo question
    @GetMapping("/question/{questionId}")
    public List<AnswerChoiceResponse> getChoicesByQuestion(@PathVariable Long questionId) {
        return answerChoiceService.getChoicesByQuestion(questionId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnswerChoiceResponse> getChoiceById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(answerChoiceService.getChoiceById(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Admin tạo đáp án
    @PostMapping
    public AnswerChoiceResponse createChoice(@RequestBody AnswerChoiceRequest request) {
        return answerChoiceService.createChoice(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnswerChoiceResponse> updateChoice(
            @PathVariable Long id,
            @RequestBody AnswerChoiceRequest request
    ) {
        try {
            return ResponseEntity.ok(answerChoiceService.updateChoice(id, request));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChoice(@PathVariable Long id) {
        answerChoiceService.deleteChoice(id);
        return ResponseEntity.noContent().build();
    }
}
