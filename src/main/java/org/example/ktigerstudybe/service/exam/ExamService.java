package org.example.ktigerstudybe.service.exam;

import org.example.ktigerstudybe.dto.req.ExamRequest;
import org.example.ktigerstudybe.dto.resp.ExamResponse;

import java.util.List;

public interface ExamService {

    List<ExamResponse> getAllExams();

    List<ExamResponse> getActiveExams();

    ExamResponse getExamById(Long id);

    ExamResponse createExam(ExamRequest request);

    ExamResponse updateExam(Long id, ExamRequest request);

    void deleteExam(Long id);
}
