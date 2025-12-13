package org.example.ktigerstudybe.controller;

import org.example.ktigerstudybe.dto.req.UserAnswerRequest;
import org.example.ktigerstudybe.dto.resp.UserAnswerResponse;
import org.example.ktigerstudybe.service.userAnswer.UserAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-answers")
public class UserAnswerController {

    @Autowired
    private UserAnswerService userAnswerService;

    // User trả lời câu hỏi
    @PostMapping
    public UserAnswerResponse saveUserAnswer(@RequestBody UserAnswerRequest request) {
        return userAnswerService.saveUserAnswer(request);
    }

    // Lấy toàn bộ câu trả lời của 1 attempt
    @GetMapping("/attempt/{attemptId}")
    public List<UserAnswerResponse> getAnswersByAttempt(@PathVariable Long attemptId) {
        return userAnswerService.getAnswersByAttempt(attemptId);
    }
}
