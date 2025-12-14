package org.example.ktigerstudybe.service.exam;

import org.example.ktigerstudybe.dto.req.ExamRequest;
import org.example.ktigerstudybe.dto.resp.ExamResponse;
import org.example.ktigerstudybe.model.Exam;
import org.example.ktigerstudybe.repository.ExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamServiceImpl implements ExamService {

    @Autowired
    private ExamRepository examRepository;

    // ===== Mapper =====
    private ExamResponse toResponse(Exam exam) {
        ExamResponse resp = new ExamResponse();
        resp.setExamId(exam.getExamId());
        resp.setTitle(exam.getTitle());
        resp.setExamType(exam.getExamType());
        resp.setTotalQuestion(exam.getTotalQuestion());
        resp.setDurationMinutes(exam.getDurationMinutes());
        resp.setIsActive(exam.getIsActive());
        return resp;
    }

    private Exam toEntity(ExamRequest request) {
        Exam exam = new Exam();
        exam.setTitle(request.getTitle());
        exam.setExamType(request.getExamType());
        exam.setTotalQuestion(request.getTotalQuestion());
        exam.setDurationMinutes(request.getDurationMinutes());
        exam.setIsActive(
                request.getIsActive() != null ? request.getIsActive() : true
        );
        return exam;
    }

    // ===== CRUD =====
    @Override
    public List<ExamResponse> getAllExams() {
        return examRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ExamResponse> getActiveExams() {
        return examRepository.findByIsActiveTrue()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ExamResponse getExamById(Long id) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Exam not found with id: " + id));
        return toResponse(exam);
    }

    @Override
    public ExamResponse createExam(ExamRequest request) {
        Exam exam = toEntity(request);
        exam = examRepository.save(exam);
        return toResponse(exam);
    }

    @Override
    public ExamResponse updateExam(Long id, ExamRequest request) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Exam not found with id: " + id));

        exam.setTitle(request.getTitle());
        exam.setExamType(request.getExamType());
        exam.setTotalQuestion(request.getTotalQuestion());
        exam.setDurationMinutes(request.getDurationMinutes());
        exam.setIsActive(request.getIsActive());

        exam = examRepository.save(exam);
        return toResponse(exam);
    }

    @Override
    public void deleteExam(Long id) {
        examRepository.deleteById(id);
    }
}
