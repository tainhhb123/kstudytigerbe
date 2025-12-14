package org.example.ktigerstudybe.controller;

import org.example.ktigerstudybe.dto.req.ExamRequest;
import org.example.ktigerstudybe.dto.resp.ExamResponse;
import org.example.ktigerstudybe.service.exam.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
public class ExamController {

    @Autowired
    private ExamService examService;

    // Lấy tất cả exam (admin)
    @GetMapping
    public List<ExamResponse> getAllExams() {
        return examService.getAllExams();
    }

    // Lấy exam đang active (user)
    @GetMapping("/active")
    public List<ExamResponse> getActiveExams() {
        return examService.getActiveExams();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamResponse> getExamById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(examService.getExamById(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ExamResponse createExam(@RequestBody ExamRequest request) {
        return examService.createExam(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExamResponse> updateExam(
            @PathVariable Long id,
            @RequestBody ExamRequest request
    ) {
        try {
            return ResponseEntity.ok(examService.updateExam(id, request));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExam(@PathVariable Long id) {
        examService.deleteExam(id);
        return ResponseEntity.noContent().build();
    }
}
