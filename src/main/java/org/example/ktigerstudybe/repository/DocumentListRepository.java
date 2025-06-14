package org.example.ktigerstudybe.repository;

import org.example.ktigerstudybe.model.DocumentList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface DocumentListRepository extends JpaRepository<DocumentList, Long> {

    List<DocumentList> findByUser_UserId(Long userId);

    List<DocumentList> findByIsPublic(int isPublic);

    List<DocumentList> findByTitleContainingIgnoreCaseAndIsPublic(String keyword, int isPublic);


    Page<DocumentList> findByIsPublicAndTitleContainingIgnoreCase(
            int isPublic, String keyword, Pageable pageable);

    Page<DocumentList> findByUser_UserIdAndIsPublic(
            Long userId, int isPublic, Pageable pageable);

    Page<DocumentList> findByIsPublicAndTitleContainingIgnoreCaseOrIsPublicAndUser_FullNameContainingIgnoreCase(
            int isPublic1, String titleKeyword,
            int isPublic2, String fullNameKeyword,
            Pageable pageable
    );
}
