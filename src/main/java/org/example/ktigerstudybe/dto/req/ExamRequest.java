package org.example.ktigerstudybe.dto.req;

import lombok.Data;
import org.example.ktigerstudybe.enums.ExamType;

@Data
public class ExamRequest {

    private String title;

    private ExamType examType;

    private Integer totalQuestion;

    private Integer durationMinutes;

    private Boolean isActive;
}
