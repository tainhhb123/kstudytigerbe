package org.example.ktigerstudybe.dto.resp;

import lombok.Data;

@Data
public class GrammarTheoryResponse {
    private Long grammarId;
    private Long lessonId;
    private String grammarTitle;
    private String grammarContent;
    private String grammarExample;
    // Nếu muốn trả thêm tên bài học:
    // private String lessonName;
}

