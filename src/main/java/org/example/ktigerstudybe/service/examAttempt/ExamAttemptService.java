package org.example.ktigerstudybe.service.examAttempt;

import org.example.ktigerstudybe.dto.req.ExamAttemptRequest;
import org.example.ktigerstudybe.dto.resp.ExamAttemptResponse;

import java.util.List;

public interface ExamAttemptService {

    ExamAttemptResponse startExam(ExamAttemptRequest request);

    ExamAttemptResponse getAttemptById(Long id);

    List<ExamAttemptResponse> getAttemptsByUser(Long userId);

    ExamAttemptResponse submitExam(Long attemptId);

}
