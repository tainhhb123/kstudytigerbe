package org.example.ktigerstudybe.dto.req;

import lombok.Data;

@Data
public class UserAnswerRequest {

    private Long attemptId;

    private Long questionId;

    // MCQ
    private Long choiceId;

    // SHORT / ESSAY
    private String answerText;
}
