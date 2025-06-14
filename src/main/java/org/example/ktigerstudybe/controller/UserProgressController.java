package org.example.ktigerstudybe.controller;

import org.example.ktigerstudybe.dto.resp.UserProgressResponse;
import org.example.ktigerstudybe.service.userprocess.UserProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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


    @GetMapping("/paged")
    public ResponseEntity<Page<UserProgressResponse>> getProgressPaged(
            @PageableDefault(size = 10, sort = "lastAccessed", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        Page<UserProgressResponse> page = userProgressService.getAllUserProgress(pageable);
        return ResponseEntity.ok(page);
    }


}
