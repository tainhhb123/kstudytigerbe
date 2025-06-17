// src/main/java/org/example/ktigerstudybe/service/documentList/DocumentListServiceImpl.java
package org.example.ktigerstudybe.service.documentList;

import org.example.ktigerstudybe.dto.req.DocumentListRequest;
import org.example.ktigerstudybe.dto.resp.DocumentListResponse;
import org.example.ktigerstudybe.model.DocumentItem;
import org.example.ktigerstudybe.model.DocumentList;
import org.example.ktigerstudybe.model.User;
import org.example.ktigerstudybe.repository.DocumentItemRepository;
import org.example.ktigerstudybe.repository.DocumentListRepository;
import org.example.ktigerstudybe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class DocumentListServiceImpl implements DocumentListService {

    private final DocumentListRepository documentListRepository;
    private final UserRepository userRepository;
    private final DocumentItemRepository documentItemRepository;

    @Autowired
    public DocumentListServiceImpl(DocumentListRepository documentListRepository,
                                   UserRepository userRepository,
                                   DocumentItemRepository documentItemRepository) {
        this.documentListRepository = documentListRepository;
        this.userRepository = userRepository;
        this.documentItemRepository = documentItemRepository;
    }

    /** Chuyển DocumentList entity → DTO */
    private DocumentListResponse toResponse(DocumentList e) {
        DocumentListResponse r = new DocumentListResponse();
        r.setListId(e.getListId());
        r.setUserId(e.getUser().getUserId());
        r.setFullName(e.getUser().getFullName());
        r.setAvatarImage(e.getUser().getAvatarImage());
        r.setTitle(e.getTitle());
        r.setDescription(e.getDescription());
        r.setType(e.getType());
        r.setCreatedAt(e.getCreatedAt());
        r.setIsPublic(e.getIsPublic());
        return r;
    }

    @Override
    @Transactional
    public DocumentListResponse createDocumentList(DocumentListRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "User not found: " + request.getUserId()));

        DocumentList list = DocumentList.builder()
                .user(user)
                .title(request.getTitle())
                .description(request.getDescription())
                .type(request.getType())
                .isPublic(request.getIsPublic())
                .build();
        list = documentListRepository.save(list);

        if (request.getItems() != null) {
            for (var it : request.getItems()) {
                DocumentItem item = DocumentItem.builder()
                        .documentList(list)
                        .word(it.getWord())
                        .meaning(it.getMeaning())
                        .example(it.getExample())
                        .vocabImage(it.getVocabImage())
                        .build();
                documentItemRepository.save(item);
            }
        }

        return toResponse(list);
    }

    @Override
    public List<DocumentListResponse> getAllDocumentLists() {
        return documentListRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DocumentListResponse getDocumentListById(Long id) {
        DocumentList e = documentListRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        "DocumentList not found: " + id));
        return toResponse(e);
    }

    @Override
    public DocumentListResponse updateDocumentList(Long id, DocumentListRequest req) {
        DocumentList e = documentListRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        "DocumentList not found: " + id));
        e.setTitle(req.getTitle());
        e.setDescription(req.getDescription());
        e.setType(req.getType());
        e.setIsPublic(req.getIsPublic());
        // Note: createdAt is set once at persist
        DocumentList updated = documentListRepository.save(e);
        return toResponse(updated);
    }

    @Override
    public void deleteDocumentList(Long id) {
        if (!documentListRepository.existsById(id)) {
            throw new NoSuchElementException(
                    "Cannot delete, not found: " + id);
        }
        documentListRepository.deleteById(id);
    }

    @Override
    public List<DocumentListResponse> getDocumentListsByUserId(Long userId) {
        return documentListRepository.findByUser_UserId(userId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DocumentListResponse> getPublicLists() {
        // isPublic == 0 xem là public
        return documentListRepository.findAllByIsPublic(0)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DocumentListResponse> getByTypeAndPublic(String type, int isPublic) {
        return documentListRepository.findByTypeAndIsPublic(type, isPublic)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getDistinctTypes() {
        return documentListRepository.findDistinctTypes();
    }

    @Override
    public Map<String, List<DocumentListResponse>> getGroupedByType(int limit) {
        List<String> types = getDistinctTypes();
        Map<String, List<DocumentListResponse>> map = new LinkedHashMap<>();
        for (String t : types) {
            List<DocumentListResponse> slice = documentListRepository
                    .findByTypeAndIsPublic(t, 0)
                    .stream()
                    .limit(limit)
                    .map(this::toResponse)
                    .collect(Collectors.toList());
            map.put(t, slice);
        }
        return map;
    }

    //Admin
    @Override
    public Page<DocumentListResponse> listByUser(Long userId, Pageable pg) {
        return documentListRepository
                .findByUser_UserIdAndIsPublic(userId, 1, pg)
                .map(this::toResponse);
    }

    @Override
    public Page<DocumentListResponse> searchPublic(String keyword, Pageable pageable) {
        String kw = keyword == null ? "" : keyword.trim();
        return documentListRepository
                .findByIsPublicAndTitleContainingIgnoreCaseOrIsPublicAndUser_FullNameContainingIgnoreCase(
                        1, kw,
                        1, kw,
                        pageable
                )
                .map(this::toResponse);
    }

}
