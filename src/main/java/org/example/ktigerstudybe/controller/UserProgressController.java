package org.example.ktigerstudybe.controller;


import org.example.ktigerstudybe.dto.req.UserProgressRequest;
import org.example.ktigerstudybe.service.userprogress.UserProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user-progress")
public class UserProgressController {

    @Autowired
    private UserProgressService userProgressService;

    @PostMapping("/complete")
    public ResponseEntity<Map<String, String>> completeLesson(@RequestBody UserProgressRequest request) {
        userProgressService.completeLesson(request.getUserId(), request.getLessonId());

        Map<String, String> response = new HashMap<>();
        response.put("message", "Tiến trình học đã được cập nhật");
        return ResponseEntity.ok(response);
    }
}