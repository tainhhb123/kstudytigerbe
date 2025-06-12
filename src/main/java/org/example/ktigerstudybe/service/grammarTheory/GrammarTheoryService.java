package org.example.ktigerstudybe.service.grammarTheory;

import org.example.ktigerstudybe.dto.req.GrammarTheoryRequest;
import org.example.ktigerstudybe.dto.resp.GrammarTheoryResponse;

import java.util.List;

public interface GrammarTheoryService {
    List<GrammarTheoryResponse> getAllGrammarTheories();
    GrammarTheoryResponse getGrammarTheoryById(Long id);
    GrammarTheoryResponse createGrammarTheory(GrammarTheoryRequest request);
    GrammarTheoryResponse updateGrammarTheory(Long id, GrammarTheoryRequest request);
    void deleteGrammarTheory(Long id);
}
