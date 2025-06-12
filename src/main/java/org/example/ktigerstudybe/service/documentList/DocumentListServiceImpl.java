package org.example.ktigerstudybe.service.documentList;

import org.example.ktigerstudybe.dto.req.DocumentListRequest;
import org.example.ktigerstudybe.dto.resp.DocumentListResponse;
import org.example.ktigerstudybe.model.DocumentList;
import org.example.ktigerstudybe.model.User;
import org.example.ktigerstudybe.repository.DocumentListRepository;
import org.example.ktigerstudybe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentListServiceImpl implements DocumentListService {

    private final DocumentListRepository documentListRepository;
    private final UserRepository userRepository;

    @Autowired
    public DocumentListServiceImpl(DocumentListRepository documentListRepository, UserRepository userRepository) {
        this.documentListRepository = documentListRepository;
        this.userRepository = userRepository;
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
        resp.setTitle(entity.getTitle());
        resp.setDescription(entity.getDescription());
        resp.setType(entity.getType());
        resp.setCreatedAt(entity.getCreatedAt());
        resp.setIsPublic(entity.getIsPublic());
        return resp;
    }

    @Override
    public DocumentListResponse createDocumentList(DocumentListRequest request) {
        DocumentList entity = toEntity(request);
        entity = documentListRepository.save(entity);
        return toResponse(entity);
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

}
