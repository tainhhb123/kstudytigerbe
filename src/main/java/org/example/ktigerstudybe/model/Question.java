package org.example.ktigerstudybe.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.ktigerstudybe.enums.QuestionType;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long questionId;

    // ===== FK tới ExamSection =====
    @ManyToOne
    @JoinColumn(name = "section_id", nullable = false)
    private ExamSection section;

    /**
     * group_id:
     * - NULL: câu đơn lẻ
     * - NOT NULL: nhóm câu (đoạn văn / audio chung)
     */
    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "question_number", nullable = false)
    private Integer questionNumber;

    // ===== ENUM: MCQ / SHORT / ESSAY =====
    @Enumerated(EnumType.STRING)
    @Column(name = "question_type", nullable = false)
    private QuestionType questionType;

    @Column(name = "question_text", columnDefinition = "TEXT")
    private String questionText;

    @Column(name = "passage_text", columnDefinition = "TEXT")
    private String passageText;

    @Column(name = "audio_url", length = 500)
    private String audioUrl;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    /**
     * correct_answer:
     * - MCQ: A / B / C / D hoặc 1 / 2 / 3 / 4
     * - SHORT: text ngắn
     * - ESSAY: thường NULL (chấm tay)
     */
    @Column(name = "correct_answer", length = 10)
    private String correctAnswer;

    @Column(name = "points", precision = 5, scale = 2)
    private BigDecimal points = BigDecimal.valueOf(1.0);
}
