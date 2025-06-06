package org.example.ktigerstudybe.repository;

import org.example.ktigerstudybe.model.DocumentItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentItemRepository extends JpaRepository<DocumentItem, Long> {

    // Lấy danh sách DocumentItem theo ListID
    List<DocumentItem> findByDocumentList_ListId(Long listId);

    // Xoá tất cả DocumentItem theo ListID
    void deleteByDocumentList_ListId(Long listId);
}
