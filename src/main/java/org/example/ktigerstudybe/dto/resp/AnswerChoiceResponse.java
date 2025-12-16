package org.example.ktigerstudybe.dto.resp;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AnswerChoiceResponse {

    private Long choiceId;

    private Long questionId;

    private String choiceLabel;

    private String choiceText;
    private Boolean isCorrect;
}
