package org.example.ktigerstudybe.dto.resp;

import lombok.Data;
import org.example.ktigerstudybe.enums.ExamType;
import org.example.ktigerstudybe.enums.SectionType;

@Data
public class ExamSectionResponse {

    private Long sectionId;

    private Long examId;
    private String examTitle;

    private SectionType sectionType;
    private ExamType examType;

    private Integer sectionOrder;
    private Integer totalQuestions;
    private Integer durationMinutes;
}
