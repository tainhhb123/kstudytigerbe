package org.example.ktigerstudybe.dto.req;

import lombok.Data;
import org.example.ktigerstudybe.enums.QuestionType;

import java.math.BigDecimal;

@Data
public class QuestionRequest {

    private Long sectionId;

    private Long groupId;

    private Integer questionNumber;

    private QuestionType questionType;

    private String questionText;

    private String passageText;

    private String audioUrl;

    private String imageUrl;

    private String correctAnswer;

    private BigDecimal points;
}
