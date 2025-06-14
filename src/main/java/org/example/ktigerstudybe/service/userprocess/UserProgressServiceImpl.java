package org.example.ktigerstudybe.service.userprocess;

import org.example.ktigerstudybe.dto.resp.UserProgressResponse;
import org.example.ktigerstudybe.model.UserProgress;
import org.example.ktigerstudybe.repository.UserProgressRepository;
import org.example.ktigerstudybe.service.userprocess.UserProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserProgressServiceImpl implements UserProgressService {

    @Autowired
    private UserProgressRepository userProgressRepository;

    private UserProgressResponse mapToDto(UserProgress up) {
        UserProgressResponse dto = new UserProgressResponse();
        dto.setUserId(up.getUser().getUserId());
        dto.setAvatarImage(up.getUser().getAvatarImage());
        dto.setFullName(up.getUser().getFullName());
        dto.setJoinDate(up.getUser().getJoinDate());
        dto.setLevelName(up.getLesson().getLevel().getLevelName());
        dto.setLessonName(up.getLesson().getLessonName());
        return dto;
    }

    @Override
    public UserProgressResponse getProgressByUserId(Long userId) {
        UserProgress up = userProgressRepository.findByUserId(userId);
        return mapToDto(up);
    }

    @Override
    public Page<UserProgressResponse> getAllUserProgress(Pageable pageable) {
        return userProgressRepository.findAllProgress(pageable)
                .map(this::mapToDto);
    }

    @Override
    public List<UserProgressResponse> getAllUserProgress() {
        List<UserProgress> progresses = userProgressRepository.findAllProgress();
        return progresses.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}
