package org.example.ktigerstudybe.service.examSection;

import org.example.ktigerstudybe.dto.req.ExamSectionRequest;
import org.example.ktigerstudybe.dto.resp.ExamSectionResponse;

import java.util.List;

public interface ExamSectionService {

    List<ExamSectionResponse> getSectionsByExam(Long examId);

    ExamSectionResponse getSectionById(Long id);

    ExamSectionResponse createSection(ExamSectionRequest request);

    ExamSectionResponse updateSection(Long id, ExamSectionRequest request);

    void deleteSection(Long id);
}
