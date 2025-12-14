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


}
