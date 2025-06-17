// src/main/java/org/example/ktigerstudybe/repository/DocumentListRepository.java
package org.example.ktigerstudybe.repository;

import org.example.ktigerstudybe.model.DocumentList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DocumentListRepository extends JpaRepository<DocumentList, Long> {

    // Lấy theo user
    List<DocumentList> findByUser_UserId(Long userId);

    // Lấy public lists (is_public = 0)
    List<DocumentList> findByIsPublic(int isPublic);

    // Lấy theo type + public flag
    List<DocumentList> findByTypeAndIsPublic(String type, int isPublic);

    // Lấy các type distinct
    @Query("SELECT DISTINCT d.type FROM DocumentList d")
    List<String> findDistinctTypes();

    List<DocumentList> findAllByIsPublic(int isPublic);

    //admin
    Page<DocumentList> findByIsPublicAndTitleContainingIgnoreCase(
            int isPublic, String keyword, Pageable pageable);

    Page<DocumentList> findByUser_UserIdAndIsPublic(
            Long userId, int isPublic, Pageable pageable);

    Page<DocumentList> findByIsPublicAndTitleContainingIgnoreCaseOrIsPublicAndUser_FullNameContainingIgnoreCase(
            int isPublic, String titleKeyword,
            int isPublic2, String nameKeyword,
            Pageable pageable
    );


}
