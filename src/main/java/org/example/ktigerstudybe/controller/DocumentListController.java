// src/main/java/org/example/ktigerstudybe/controller/DocumentListController.java
package org.example.ktigerstudybe.controller;

import org.example.ktigerstudybe.dto.req.DocumentListRequest;
import org.example.ktigerstudybe.dto.resp.DocumentListResponse;
import org.example.ktigerstudybe.service.documentList.DocumentListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/document-lists")
public class DocumentListController {

    @Autowired
    private DocumentListService documentListService;

    // Unpaged: get all
    @GetMapping
    public List<DocumentListResponse> getAllDocumentLists() {
        return documentListService.getAllDocumentLists();
    }

    // Unpaged: get by id
    @GetMapping("/{id}")
    public ResponseEntity<DocumentListResponse> getDocumentListById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(documentListService.getDocumentListById(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Create
    @PostMapping
    public DocumentListResponse createDocumentList(@RequestBody DocumentListRequest request) {
        return documentListService.createDocumentList(request);
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<DocumentListResponse> updateDocumentList(
            @PathVariable Long id,
            @RequestBody DocumentListRequest request) {
        try {
            return ResponseEntity.ok(documentListService.updateDocumentList(id, request));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocumentList(@PathVariable Long id) {
        documentListService.deleteDocumentList(id);
        return ResponseEntity.noContent().build();
    }

    // Unpaged: get lists by user
    @GetMapping("/user/{userId}")
    public List<DocumentListResponse> getDocumentListsByUserId(@PathVariable Long userId) {
        return documentListService.getDocumentListsByUserId(userId);
    }

    // Unpaged: get public lists
    @GetMapping("/public")
    public List<DocumentListResponse> getPublicDocumentLists() {
        return documentListService.getPublicDocumentLists();
    }

    // Unpaged: search by title
    @GetMapping("/search")
    public List<DocumentListResponse> searchByTitle(@RequestParam String keyword) {
        return documentListService.searchByTitle(keyword);
    }



    // Paged: public with optional search
    @GetMapping("/public/paged")
    public Page<DocumentListResponse> getPublicPaged(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return documentListService.searchPublic(keyword, pageable);
    }

    // Paged: lists by user
    @GetMapping("/user/{userId}/paged")
    public Page<DocumentListResponse> getByUserPaged(
            @PathVariable Long userId,
            @PageableDefault(size = 5) Pageable pageable
    ) {
        return documentListService.listByUser(userId, pageable);
    }

    // Paged: lists by user + search
    @GetMapping("/user/{userId}/search/paged")
    public Page<DocumentListResponse> searchByUserPaged(
            @PathVariable Long userId,
            @RequestParam String keyword,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return documentListService.searchPublic(keyword, pageable);
    }
}
