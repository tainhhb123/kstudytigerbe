package org.example.ktigerstudybe.dto.resp;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserAnswerResponse {

    private Long userAnswerId;

    private Long attemptId;

    private Long questionId;

    private Long choiceId;

    private String answerText;

    private BigDecimal score;
}
