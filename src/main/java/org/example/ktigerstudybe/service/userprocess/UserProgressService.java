package org.example.ktigerstudybe.service.userprocess;

import org.example.ktigerstudybe.dto.resp.UserProgressResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserProgressService {
    UserProgressResponse getProgressByUserId(Long userId);
    List<UserProgressResponse> getAllUserProgress();
    Page<UserProgressResponse> getAllUserProgress(Pageable pageable);

}
