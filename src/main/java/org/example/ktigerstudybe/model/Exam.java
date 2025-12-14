package org.example.ktigerstudybe.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.ktigerstudybe.enums.ExamType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "exam")
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exam_id")
    private Long examId;

    @Column(name = "title", nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "exam_type", nullable = false)
    private ExamType examType;

    @Column(name = "total_question", nullable = false)
    private Integer totalQuestion;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @Column(name = "is_active")
    private Boolean isActive = true;
}
