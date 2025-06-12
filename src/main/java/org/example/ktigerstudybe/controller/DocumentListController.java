package org.example.ktigerstudybe.controller;

import org.example.ktigerstudybe.dto.req.DocumentListRequest;
import org.example.ktigerstudybe.dto.resp.DocumentListResponse;
import org.example.ktigerstudybe.service.documentList.DocumentListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/document-lists")
public class DocumentListController {

    @Autowired
    private DocumentListService documentListService;

    @GetMapping
    public List<DocumentListResponse> getAllDocumentLists() {
        return documentListService.getAllDocumentLists();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentListResponse> getDocumentListById(@PathVariable Long id) {
        try {
            DocumentListResponse resp = documentListService.getDocumentListById(id);
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public DocumentListResponse createDocumentList(@RequestBody DocumentListRequest request) {
        return documentListService.createDocumentList(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentListResponse> updateDocumentList(
            @PathVariable Long id,
            @RequestBody DocumentListRequest request) {
        try {
            DocumentListResponse updated = documentListService.updateDocumentList(id, request);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocumentList(@PathVariable Long id) {
        documentListService.deleteDocumentList(id);
        return ResponseEntity.noContent().build();
    }

    // Thêm: Lấy theo userId
    @GetMapping("/user/{userId}")
    public List<DocumentListResponse> getDocumentListsByUserId(@PathVariable Long userId) {
        return documentListService.getDocumentListsByUserId(userId);
    }

    // Thêm: Lấy danh sách công khai
    @GetMapping("/public")
    public List<DocumentListResponse> getPublicDocumentLists() {
        return documentListService.getPublicDocumentLists();
    }

    // Thêm: Tìm kiếm theo tiêu đề
    @GetMapping("/search")
    public List<DocumentListResponse> searchByTitle(@RequestParam String keyword) {
        return documentListService.searchByTitle(keyword);
    }
}
