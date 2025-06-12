package org.example.ktigerstudybe.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SentenceRewritingQuestionRequest {

    private Long exerciseId;

//    @NotBlank (message = "Kh dc de trong câu gốc")
    private String originalSentence;
    private String rewrittenSentence;
    private String linkMedia;
}
