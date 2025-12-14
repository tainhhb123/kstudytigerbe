package org.example.ktigerstudybe.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "answer_choice")
public class AnswerChoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "choice_id")
    private Long choiceId;

    // ===== FK tới Question =====
    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    /**
     * A / B / C / D
     * hoặc ① / ② / ③ / ④
     */
    @Column(name = "choice_label", nullable = false, length = 10)
    private String choiceLabel;

    @Column(name = "choice_text", nullable = false, columnDefinition = "TEXT")
    private String choiceText;

    @Column(name = "is_correct")
    private Boolean isCorrect = false;
}
