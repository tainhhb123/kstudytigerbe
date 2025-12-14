package org.example.ktigerstudybe.service.examAttempt;

import org.example.ktigerstudybe.dto.req.ExamAttemptRequest;
import org.example.ktigerstudybe.dto.resp.ExamAttemptResponse;
import org.example.ktigerstudybe.enums.ExamAttemptStatus;
import org.example.ktigerstudybe.model.Exam;
import org.example.ktigerstudybe.model.ExamAttempt;
import org.example.ktigerstudybe.model.User;
import org.example.ktigerstudybe.repository.ExamAttemptRepository;
import org.example.ktigerstudybe.repository.ExamRepository;
import org.example.ktigerstudybe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamAttemptServiceImpl implements ExamAttemptService {

    @Autowired
    private ExamAttemptRepository examAttemptRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private UserRepository userRepository;

    // ===== Mapper =====
    private ExamAttemptResponse toResponse(ExamAttempt attempt) {
        ExamAttemptResponse resp = new ExamAttemptResponse();

        resp.setAttemptId(attempt.getAttemptId());

        resp.setExamId(attempt.getExam().getExamId());
        resp.setExamTitle(attempt.getExam().getTitle());

        resp.setUserId(attempt.getUser().getUserId());
        resp.setUserName(attempt.getUser().getUserName());

        resp.setStartTime(attempt.getStartTime());
        resp.setEndTime(attempt.getEndTime());

        resp.setStatus(attempt.getStatus().name());

        resp.setListeningScore(attempt.getListeningScore());
        resp.setReadingScore(attempt.getReadingScore());
        resp.setWritingScore(attempt.getWritingScore());
        resp.setTotalScore(attempt.getTotalScore());

        return resp;
    }

    // ===== Start exam =====
    @Override
    public ExamAttemptResponse startExam(ExamAttemptRequest request) {

        Exam exam = examRepository.findById(request.getExamId())
                .orElseThrow(() -> new IllegalArgumentException("Exam not found"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        ExamAttempt attempt = new ExamAttempt();
        attempt.setExam(exam);
        attempt.setUser(user);
        attempt.setStartTime(LocalDateTime.now());
        attempt.setStatus(ExamAttemptStatus.IN_PROGRESS);

        attempt = examAttemptRepository.save(attempt);
        return toResponse(attempt);
    }

    @Override
    public ExamAttemptResponse getAttemptById(Long id) {
        ExamAttempt attempt = examAttemptRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Attempt not found"));
        return toResponse(attempt);
    }

    @Override
    public List<ExamAttemptResponse> getAttemptsByUser(Long userId) {
        return examAttemptRepository.findByUser_UserId(userId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ===== Submit exam =====
    @Override
    public ExamAttemptResponse submitExam(Long attemptId) {

        ExamAttempt attempt = examAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new IllegalArgumentException("Attempt not found"));

        attempt.setEndTime(LocalDateTime.now());
        attempt.setStatus(ExamAttemptStatus.COMPLETED);

        // 👉 Điểm sẽ tính ở bước UserAnswer
        attempt = examAttemptRepository.save(attempt);
        return toResponse(attempt);
    }
}
