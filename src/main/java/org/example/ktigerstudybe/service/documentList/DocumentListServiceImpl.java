package org.example.ktigerstudybe.service.documentList;

import jakarta.transaction.Transactional;
import org.example.ktigerstudybe.dto.req.DocumentItemRequest;
import org.example.ktigerstudybe.dto.req.DocumentListRequest;
import org.example.ktigerstudybe.dto.resp.DocumentListResponse;
import org.example.ktigerstudybe.model.DocumentItem;
import org.example.ktigerstudybe.model.DocumentList;
import org.example.ktigerstudybe.model.User;
import org.example.ktigerstudybe.repository.DocumentItemRepository;
import org.example.ktigerstudybe.repository.DocumentListRepository;
import org.example.ktigerstudybe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentListServiceImpl implements DocumentListService {

    private final DocumentListRepository documentListRepository;
    private final UserRepository userRepository;

    private final DocumentItemRepository documentItemRepository;



    @Autowired
    public DocumentListServiceImpl(DocumentListRepository documentListRepository, UserRepository userRepository,  DocumentItemRepository documentItemRepository) {
        this.documentListRepository = documentListRepository;
        this.userRepository = userRepository;
        this.documentItemRepository = documentItemRepository;
    }

    // DTO → Entity
    private DocumentList toEntity(DocumentListRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + request.getUserId()));

        DocumentList entity = new DocumentList();
        entity.setUser(user);
        entity.setTitle(request.getTitle());
        entity.setDescription(request.getDescription());
        entity.setType(request.getType());
        entity.setCreatedAt(request.getCreatedAt());
        entity.setIsPublic(request.getIsPublic());
        return entity;
    }

    // Entity → DTO
    private DocumentListResponse toResponse(DocumentList entity) {
        DocumentListResponse resp = new DocumentListResponse();
        resp.setListId(entity.getListId());
        resp.setUserId(entity.getUser().getUserId());
        resp.setFullName(entity.getUser().getFullName());
        resp.setTitle(entity.getTitle());
        resp.setDescription(entity.getDescription());
        resp.setType(entity.getType());
        resp.setCreatedAt(entity.getCreatedAt());
        resp.setIsPublic(entity.getIsPublic());
        return resp;
    }

    @Override
    @Transactional
    public DocumentListResponse createDocumentList(DocumentListRequest request) {
        // 1. Kiểm tra và lấy User
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User không tồn tại với ID: " + request.getUserId()));

        // 2. Tạo và lưu DocumentList
        DocumentList list = DocumentList.builder()
                .user(user)
                .title(request.getTitle())
                .description(request.getDescription())
                .type(request.getType())
                .isPublic(request.getIsPublic())
                .build();
        list = documentListRepository.save(list);

        // 3. Duyệt và lưu từng DocumentItem (nếu có)
        if (request.getItems() != null && !request.getItems().isEmpty()) {
            for (DocumentItemRequest it : request.getItems()) {
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

        // 4. Chuyển sang DTO và trả về
        return toResponse(list);
    }

    @Override
    public List<DocumentListResponse> getAllDocumentLists() {
        return documentListRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DocumentListResponse getDocumentListById(Long listId) {
        DocumentList entity = documentListRepository.findById(listId)
                .orElseThrow(() -> new IllegalArgumentException("DocumentList not found with ID: " + listId));
        return toResponse(entity);
    }

    @Override
    public DocumentListResponse updateDocumentList(Long listId, DocumentListRequest request) {
        DocumentList entity = documentListRepository.findById(listId)
                .orElseThrow(() -> new IllegalArgumentException("DocumentList not found with ID: " + listId));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + request.getUserId()));

        entity.setUser(user);
        entity.setTitle(request.getTitle());
        entity.setDescription(request.getDescription());
        entity.setType(request.getType());
        entity.setCreatedAt(request.getCreatedAt());
        entity.setIsPublic(request.getIsPublic());

        entity = documentListRepository.save(entity);
        return toResponse(entity);
    }

    @Override
    public void deleteDocumentList(Long listId) {
        if (!documentListRepository.existsById(listId)) {
            throw new IllegalArgumentException("Cannot delete: DocumentList not found with ID: " + listId);
        }
        documentListRepository.deleteById(listId);
    }

    @Override
    public List<DocumentListResponse> getDocumentListsByUserId(Long userId) {
        return documentListRepository.findByUser_UserId(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DocumentListResponse> getPublicDocumentLists() {
        return documentListRepository.findByIsPublic(1).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DocumentListResponse> searchByTitle(String keyword) {
        return documentListRepository
                .findByTitleContainingIgnoreCaseAndIsPublic(keyword, 1)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
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
