// src/main/java/org/example/ktigerstudybe/repository/DocumentListRepository.java
package org.example.ktigerstudybe.repository;

import org.example.ktigerstudybe.model.DocumentList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DocumentListRepository extends JpaRepository<DocumentList, Long> {

    // Lấy theo user
    List<DocumentList> findByUser_UserId(Long userId);

    // Phân trang các bản ghi public (isPublic = 0)
    Page<DocumentList> findByIsPublic(int isPublic, Pageable pageable);

    // Lấy theo type + public flag
    List<DocumentList> findByTypeAndIsPublic(String type, int isPublic);

    // Lấy danh sách các type duy nhất
    @Query("SELECT DISTINCT d.type FROM DocumentList d")
    List<String> findDistinctTypes();

    // Lấy toàn bộ public lists không phân trang
    List<DocumentList> findAllByIsPublic(int isPublic);
}
