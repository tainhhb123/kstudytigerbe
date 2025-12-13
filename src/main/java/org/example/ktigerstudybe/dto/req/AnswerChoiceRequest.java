package org.example.ktigerstudybe.dto.req;

import lombok.Data;

@Data
public class AnswerChoiceRequest {

    private Long questionId;

    private String choiceLabel;

    private String choiceText;

    private Boolean isCorrect;
}
