package org.example.ktigerstudybe.repository;

import org.example.ktigerstudybe.model.DocumentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentReportRepository extends JpaRepository<DocumentReport, Long> {

    // Tìm tất cả báo cáo theo userId
    List<DocumentReport> findByUser_UserId(Long userId);

    // Tìm tất cả báo cáo theo listId (DocumentList ID)
    List<DocumentReport> findByDocumentList_ListId(Long listId);
}
