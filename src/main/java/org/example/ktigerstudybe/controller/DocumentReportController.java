package org.example.ktigerstudybe.controller;

import org.example.ktigerstudybe.dto.req.DocumentReportRequest;
import org.example.ktigerstudybe.dto.resp.DocumentReportResponse;
import org.example.ktigerstudybe.service.documentReport.DocumentReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/document-reports")
public class DocumentReportController {

    @Autowired
    private DocumentReportService documentReportService;

    // Lấy tất cả các báo cáo
    @GetMapping
    public List<DocumentReportResponse> getAllReports() {
        return documentReportService.getAllReports();
    }

    // Lấy báo cáo theo ID
    @GetMapping("/{id}")
    public ResponseEntity<DocumentReportResponse> getReportById(@PathVariable Long id) {
        try {
            DocumentReportResponse response = documentReportService.getReportById(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Tạo mới một báo cáo
    @PostMapping
    public DocumentReportResponse createReport(@RequestBody DocumentReportRequest request) {
        return documentReportService.createReport(request);
    }

    // Xóa một báo cáo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        try {
            documentReportService.deleteReport(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Lấy các báo cáo theo userId
    @GetMapping("/user/{userId}")
    public List<DocumentReportResponse> getReportsByUserId(@PathVariable Long userId) {
        return documentReportService.getReportsByUserId(userId);
    }

    // Lấy các báo cáo theo listId (ID tài liệu)
    @GetMapping("/document/{listId}")
    public List<DocumentReportResponse> getReportsByListId(@PathVariable Long listId) {
        return documentReportService.getReportsByListId(listId);
    }
}
