// src/main/java/org/example/ktigerstudybe/controller/DocumentListController.java
package org.example.ktigerstudybe.controller;

import org.example.ktigerstudybe.dto.req.DocumentListRequest;
import org.example.ktigerstudybe.dto.resp.DocumentListResponse;
import org.example.ktigerstudybe.service.documentList.DocumentListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/document-lists")
public class DocumentListController {

    private final DocumentListService service;

    @Autowired
    public DocumentListController(DocumentListService service) {
        this.service = service;
    }

    /**
     * 1) Lấy tất cả các bộ public (is_public = 0)
     */
    // Phân trang công khai

    @GetMapping("/public")
    public Page<DocumentListResponse> getPublicLists(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);  // bạn có thể thêm Sort ở đây nếu cần
        return service.getPublicLists(pageable);
    }

    /**
     * 2) Lấy danh sách các loại (distinct types)
     */
//    @GetMapping("/distinct-types")
//    public List<String> getDistinctTypes() {
//        return service.getDistinctTypes();
//    }

    /**
     * 3) Lấy grouped theo type, mỗi type tối đa 4 items
     */
    @GetMapping("/grouped")
    public Map<String, List<DocumentListResponse>> getGroupedByType() {
        return service.getGroupedByType(4);
    }

    /**
     * 4) Lấy theo type, filter isPublic (mặc định 0 = public)
     */
    @GetMapping("/type/{type}")
    public List<DocumentListResponse> getByTypeAndPublic(
            @PathVariable String type,
            @RequestParam(defaultValue = "0") int isPublic
    ) {
        return service.getByTypeAndPublic(type, isPublic);
    }

    /**
     * 5) Lấy theo userId
     */
    @GetMapping("/user/{userId}")
    public List<DocumentListResponse> getByUser(@PathVariable Long userId) {
        return service.getDocumentListsByUserId(userId);
    }

    /**
     * 6) Lấy chi tiết theo id (chỉ khớp số)
     */
    @GetMapping("/{id:\\d+}")
    public DocumentListResponse getById(@PathVariable Long id) {
        return service.getDocumentListById(id);
    }

    /**
     * 7) Tạo mới
     */
    @PostMapping
    public DocumentListResponse create(@RequestBody DocumentListRequest request) {
        return service.createDocumentList(request);
    }

    /**
     * 8) Cập nhật theo id (chỉ khớp số)
     */
    @PutMapping("/{id:\\d+}")
    public DocumentListResponse update(
            @PathVariable Long id,
            @RequestBody DocumentListRequest request
    ) {
        return service.updateDocumentList(id, request);
    }

    /**
     * 9) Xóa theo id (chỉ khớp số)
     */
    @DeleteMapping("/{id:\\d+}")
    public void delete(@PathVariable Long id) {
        service.deleteDocumentList(id);
    }
}
