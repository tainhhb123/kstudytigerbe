package org.example.ktigerstudybe.dto.resp;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ClassDocumentListResponse {
    private Long classDocumentListId;
    private Long classId;
    private String className;
    private Long listId;
    private String listTitle;
    private LocalDateTime assignedAt;
}
