package org.example.ktigerstudybe.dto.resp;

import lombok.Data;
import org.example.ktigerstudybe.enums.QuestionType;

import java.math.BigDecimal;

@Data
public class QuestionResponse {

    private Long questionId;

    private Long sectionId;

    private Long groupId;

    private Integer questionNumber;

    private QuestionType questionType;

    private String questionText;

    private String passageText;

    private String audioUrl;

    private String imageUrl;

    private BigDecimal points;
}
