package org.example.ktigerstudybe.service.examSection;

import org.example.ktigerstudybe.dto.req.ExamSectionRequest;
import org.example.ktigerstudybe.dto.resp.ExamSectionResponse;
import org.example.ktigerstudybe.model.Exam;
import org.example.ktigerstudybe.model.ExamSection;
import org.example.ktigerstudybe.repository.ExamRepository;
import org.example.ktigerstudybe.repository.ExamSectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamSectionServiceImpl implements ExamSectionService {

    @Autowired
    private ExamSectionRepository examSectionRepository;

    @Autowired
    private ExamRepository examRepository;

    // ===== Mapper =====
    private ExamSectionResponse toResponse(ExamSection section) {
        ExamSectionResponse resp = new ExamSectionResponse();

        resp.setSectionId(section.getSectionId());

        resp.setExamId(section.getExam().getExamId());
        resp.setExamTitle(section.getExam().getTitle());

        resp.setSectionType(section.getSectionType());
        resp.setExamType(section.getExamType());

        resp.setSectionOrder(section.getSectionOrder());
        resp.setTotalQuestions(section.getTotalQuestions());
        resp.setDurationMinutes(section.getDurationMinutes());
        resp.setAudioUrl(section.getAudioUrl());

        return resp;
    }

    @Override
    public List<ExamSectionResponse> getSectionsByExam(Long examId) {
        return examSectionRepository
                .findByExam_ExamIdOrderBySectionOrderAsc(examId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ExamSectionResponse getSectionById(Long id) {
        ExamSection section = examSectionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ExamSection not found"));
        return toResponse(section);
    }

    @Override
    public ExamSectionResponse createSection(ExamSectionRequest request) {

        Exam exam = examRepository.findById(request.getExamId())
                .orElseThrow(() -> new IllegalArgumentException("Exam not found"));

        ExamSection section = new ExamSection();
        section.setExam(exam);
        section.setSectionType(request.getSectionType());
        section.setExamType(request.getExamType());
        section.setSectionOrder(request.getSectionOrder());
        section.setTotalQuestions(request.getTotalQuestions());
        section.setDurationMinutes(request.getDurationMinutes());

        section = examSectionRepository.save(section);
        return toResponse(section);
    }

    @Override
    public ExamSectionResponse updateSection(Long id, ExamSectionRequest request) {

        ExamSection section = examSectionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ExamSection not found"));

        section.setSectionType(request.getSectionType());
        section.setExamType(request.getExamType());
        section.setSectionOrder(request.getSectionOrder());
        section.setTotalQuestions(request.getTotalQuestions());
        section.setDurationMinutes(request.getDurationMinutes());

        section = examSectionRepository.save(section);
        return toResponse(section);
    }

    @Override
    public void deleteSection(Long id) {
        examSectionRepository.deleteById(id);
    }
}
