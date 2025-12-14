package org.example.ktigerstudybe.dto.resp;

import lombok.Data;

@Data
public class AnswerChoiceResponse {

    private Long choiceId;

    private Long questionId;

    private String choiceLabel;

    private String choiceText;
}
