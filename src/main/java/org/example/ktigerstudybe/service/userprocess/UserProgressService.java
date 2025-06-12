package org.example.ktigerstudybe.service.userprocess;

import org.example.ktigerstudybe.dto.resp.UserProgressResponse;

import java.util.List;

public interface UserProgressService {
    UserProgressResponse getProgressByUserId(Long userId);
    List<UserProgressResponse> getAllUserProgress();
}
