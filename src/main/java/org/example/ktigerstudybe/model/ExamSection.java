package org.example.ktigerstudybe.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.ktigerstudybe.enums.ExamType;
import org.example.ktigerstudybe.enums.SectionType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "exam_section")
public class ExamSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "section_id")
    private Long sectionId;

    // ===== FK tới Exam =====
    @ManyToOne
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    // ===== ENUM: LISTENING / READING / WRITING =====
    @Enumerated(EnumType.STRING)
    @Column(name = "section_type", nullable = false)
    private SectionType sectionType;

    @Column(name = "audio_url", length = 500)
    private String audioUrl;

    // ===== ENUM: TOPIK_I / TOPIK_II =====
    @Enumerated(EnumType.STRING)
    @Column(name = "exam_type", nullable = false)
    private ExamType examType;

    @Column(name = "section_order", nullable = false)
    private Integer sectionOrder;

    @Column(name = "total_questions", nullable = false)
    private Integer totalQuestions;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;


}
