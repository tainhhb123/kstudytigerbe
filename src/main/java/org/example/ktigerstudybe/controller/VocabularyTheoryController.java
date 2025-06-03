package org.example.ktigerstudybe.controller;

import org.example.ktigerstudybe.dto.req.VocabularyTheoryRequest;
import org.example.ktigerstudybe.dto.resp.VocabularyTheoryResponse;
import org.example.ktigerstudybe.service.vocabularyTheory.VocabularyTheoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vocabulary-theories")
public class VocabularyTheoryController {

    @Autowired
    private VocabularyTheoryService vocabularyTheoryService;

    // Lấy danh sách tất cả vocabulary theory
    @GetMapping
    public List<VocabularyTheoryResponse> getAllVocabularyTheories() {
        return vocabularyTheoryService.getAllVocabularyTheories();
    }

    // Lấy chi tiết một vocabulary theory theo id
    @GetMapping("/{id}")
    public ResponseEntity<VocabularyTheoryResponse> getVocabularyTheoryById(@PathVariable Long id) {
        try {
            VocabularyTheoryResponse resp = vocabularyTheoryService.getVocabularyTheoryById(id);
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Thêm mới vocabulary theory
    @PostMapping
    public VocabularyTheoryResponse createVocabularyTheory(@RequestBody VocabularyTheoryRequest request) {
        return vocabularyTheoryService.createVocabularyTheory(request);
    }

    // Sửa vocabulary theory
    @PutMapping("/{id}")
    public ResponseEntity<VocabularyTheoryResponse> updateVocabularyTheory(
            @PathVariable Long id,
            @RequestBody VocabularyTheoryRequest request) {
        try {
            VocabularyTheoryResponse updated = vocabularyTheoryService.updateVocabularyTheory(id, request);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Xóa vocabulary theory
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVocabularyTheory(@PathVariable Long id) {
        vocabularyTheoryService.deleteVocabularyTheory(id);
        return ResponseEntity.noContent().build();
    }
}
