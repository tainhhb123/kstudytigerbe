package org.example.ktigerstudybe.controller;

import org.example.ktigerstudybe.dto.req.ExamSectionRequest;
import org.example.ktigerstudybe.dto.resp.ExamSectionResponse;
import org.example.ktigerstudybe.service.examSection.ExamSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exam-sections")
public class ExamSectionController {

    @Autowired
    private ExamSectionService examSectionService;

    // Lấy section theo exam (user + admin)
    @GetMapping("/exam/{examId}")
    public List<ExamSectionResponse> getSectionsByExam(@PathVariable Long examId) {
        return examSectionService.getSectionsByExam(examId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamSectionResponse> getSectionById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(examSectionService.getSectionById(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Admin tạo section
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ExamSectionResponse createSection(@RequestBody ExamSectionRequest request) {
        return examSectionService.createSection(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ExamSectionResponse> updateSection(
            @PathVariable Long id,
            @RequestBody ExamSectionRequest request
    ) {
        try {
            return ResponseEntity.ok(examSectionService.updateSection(id, request));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSection(@PathVariable Long id) {
        examSectionService.deleteSection(id);
        return ResponseEntity.noContent().build();
    }
}
