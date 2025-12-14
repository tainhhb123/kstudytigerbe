package org.example.ktigerstudybe.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.ktigerstudybe.enums.ExamAttemptStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "exam_attempt")
public class ExamAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attempt_id")
    private Long attemptId;

    // ===== FK tới bài thi =====
    @ManyToOne
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    // ===== FK tới User =====
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ExamAttemptStatus status;

    @Column(name = "listening_score", precision = 6, scale = 2)
    private BigDecimal listeningScore;

    @Column(name = "reading_score", precision = 6, scale = 2)
    private BigDecimal readingScore;

    @Column(name = "writing_score", precision = 6, scale = 2)
    private BigDecimal writingScore;

    @Column(name = "total_score", precision = 6, scale = 2)
    private BigDecimal totalScore;

}
