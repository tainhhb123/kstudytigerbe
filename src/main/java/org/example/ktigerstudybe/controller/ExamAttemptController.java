package org.example.ktigerstudybe.controller;

import org.example.ktigerstudybe.dto.req.ExamAttemptRequest;
import org.example.ktigerstudybe.dto.resp.ExamAttemptResponse;
import org.example.ktigerstudybe.service.examAttempt.ExamAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exam-attempts")
public class ExamAttemptController {

    @Autowired
    private ExamAttemptService examAttemptService;

    // Start exam
    @PostMapping("/start")
    public ExamAttemptResponse startExam(@RequestBody ExamAttemptRequest request) {
        return examAttemptService.startExam(request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamAttemptResponse> getAttempt(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(examAttemptService.getAttemptById(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}")
    public List<ExamAttemptResponse> getAttemptsByUser(@PathVariable Long userId) {
        return examAttemptService.getAttemptsByUser(userId);
    }

    // Submit exam
    @PostMapping("/{id}/submit")
    public ExamAttemptResponse submitExam(@PathVariable Long id) {
        return examAttemptService.submitExam(id);
    }
}
