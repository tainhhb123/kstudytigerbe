package org.example.ktigerstudybe.dto.req;

import lombok.Data;
import org.example.ktigerstudybe.enums.ExamType;
import org.example.ktigerstudybe.enums.SectionType;

@Data
public class ExamSectionRequest {

    private Long examId;

    private SectionType sectionType;

    private ExamType examType;

    private Integer sectionOrder;

    private Integer totalQuestions;

    private Integer durationMinutes;
}
