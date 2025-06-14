package org.example.ktigerstudybe.repository;

import org.example.ktigerstudybe.model.FavoriteDocumentList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteDocumentListRepository extends JpaRepository<FavoriteDocumentList, Long> {
    List<FavoriteDocumentList> findByUser_UserId(Long userId);
    List<FavoriteDocumentList> findByDocumentList_ListId(Long listId);
    Optional<FavoriteDocumentList> findByUser_UserIdAndDocumentList_ListId(Long userId, Long listId);
}
