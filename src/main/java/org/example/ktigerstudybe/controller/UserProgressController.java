package org.example.ktigerstudybe.controller;

import org.example.ktigerstudybe.dto.resp.UserProgressResponse;
import org.example.ktigerstudybe.service.userprocess.UserProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-progress")
public class UserProgressController {

    @Autowired
    private UserProgressService userProgressService;

    // Lấy tiến trình học tập của một user cụ thể theo ID
    @GetMapping("/{userId}")
    public ResponseEntity<UserProgressResponse> getProgressByUserId(@PathVariable Long userId) {
        UserProgressResponse response = userProgressService.getProgressByUserId(userId);
        return ResponseEntity.ok(response);
    }

    // Lấy danh sách tiến trình học tập của tất cả user
    @GetMapping
    public ResponseEntity<List<UserProgressResponse>> getAllUserProgress() {
        List<UserProgressResponse> response = userProgressService.getAllUserProgress();
        return ResponseEntity.ok(response);
    }
}
