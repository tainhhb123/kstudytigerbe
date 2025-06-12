package org.example.ktigerstudybe.service.vocabularyTheory;

import org.example.ktigerstudybe.dto.req.VocabularyTheoryRequest;
import org.example.ktigerstudybe.dto.resp.VocabularyTheoryResponse;

import java.util.List;

public interface VocabularyTheoryService {
    List<VocabularyTheoryResponse> getAllVocabularyTheories();
    VocabularyTheoryResponse getVocabularyTheoryById(Long id);
    VocabularyTheoryResponse createVocabularyTheory(VocabularyTheoryRequest request);
    VocabularyTheoryResponse updateVocabularyTheory(Long id, VocabularyTheoryRequest request);
    void deleteVocabularyTheory(Long id);
}
