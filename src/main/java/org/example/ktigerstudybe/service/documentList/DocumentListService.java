package org.example.ktigerstudybe.service.documentList;

import org.example.ktigerstudybe.dto.req.DocumentListRequest;
import org.example.ktigerstudybe.dto.resp.DocumentListResponse;

import java.util.List;

public interface DocumentListService {

    // Lấy tất cả các DocumentList
    List<DocumentListResponse> getAllDocumentLists();

    // Lấy DocumentList theo ID
    DocumentListResponse getDocumentListById(Long listId);

    // Tạo mới một DocumentList
    DocumentListResponse createDocumentList(DocumentListRequest request);

    // Cập nhật thông tin DocumentList
    DocumentListResponse updateDocumentList(Long listId, DocumentListRequest request);

    // Xóa DocumentList theo ID
    void deleteDocumentList(Long listId);

    // Lấy các DocumentList theo userId
    List<DocumentListResponse> getDocumentListsByUserId(Long userId);

    // Lấy các DocumentList công khai
    List<DocumentListResponse> getPublicDocumentLists();

    // Tìm kiếm DocumentList theo tiêu đề
    List<DocumentListResponse> searchByTitle(String keyword);
}
