// src/main/java/org/example/ktigerstudybe/repository/DocumentListRepository.java
package org.example.ktigerstudybe.repository;

import jakarta.transaction.Transactional;
import org.example.ktigerstudybe.model.DocumentList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    List<DocumentList> findByUser_UserIdOrderByCreatedAtDesc(Long userId);

    // Tìm theo title hoặc type (LIKE, ignore case), có phân trang
    @Query("""
    SELECT d
      FROM DocumentList d
     WHERE d.isPublic = 0
       AND (
         LOWER(d.title) LIKE LOWER(CONCAT('%', :kw, '%'))
      OR LOWER(d.type ) LIKE LOWER(CONCAT('%', :kw, '%'))
       )
  """)
    Page<DocumentList> searchPublicByTitleOrType(@Param("kw") String keyword, Pageable pageable);

    /**
     * Trả về tất cả DocumentList mà user này đã đánh dấu favorite
     */
    @Query("""
      SELECT f.documentList 
        FROM FavoriteDocumentList f 
       WHERE f.user.userId = :userId
    """)
    List<DocumentList> findFavoritedByUserId(@Param("userId") Long userId);



    @Modifying
    @Transactional
    @Query("""
      UPDATE DocumentList d
         SET d.isPublic = CASE WHEN d.isPublic = 0 THEN 1 ELSE 0 END
       WHERE d.listId    = :listId
    """)
    int toggleIsPublic(@Param("listId") Long listId);
}
