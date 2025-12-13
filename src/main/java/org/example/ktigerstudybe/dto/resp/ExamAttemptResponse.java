package org.example.ktigerstudybe.dto.resp;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ExamAttemptResponse {

    private Long attemptId;

    private Long examId;
    private String examTitle;

    private Long userId;
    private String userName;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private String status;

    private BigDecimal listeningScore;
    private BigDecimal readingScore;
    private BigDecimal writingScore;
    private BigDecimal totalScore;
}
