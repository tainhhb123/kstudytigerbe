package org.example.ktigerstudybe.dto.resp;

import lombok.Data;
import org.example.ktigerstudybe.enums.ExamType;

@Data
public class ExamResponse {

    private Long examId;

    private String title;

    private ExamType examType;

    private Integer totalQuestion;

    private Integer durationMinutes;

    private Boolean isActive;
}
