package org.example.ktigerstudybe.controller;

import org.example.ktigerstudybe.dto.req.QuestionRequest;
import org.example.ktigerstudybe.dto.resp.QuestionResponse;
import org.example.ktigerstudybe.service.question.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    // Lấy câu hỏi theo section
    @GetMapping("/section/{sectionId}")
    public List<QuestionResponse> getQuestionsBySection(@PathVariable Long sectionId) {
        return questionService.getQuestionsBySection(sectionId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionResponse> getQuestionById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(questionService.getQuestionById(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Admin tạo câu hỏi
    @PostMapping
    public QuestionResponse createQuestion(@RequestBody QuestionRequest request) {
        return questionService.createQuestion(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestionResponse> updateQuestion(
            @PathVariable Long id,
            @RequestBody QuestionRequest request
    ) {
        try {
            return ResponseEntity.ok(questionService.updateQuestion(id, request));
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
