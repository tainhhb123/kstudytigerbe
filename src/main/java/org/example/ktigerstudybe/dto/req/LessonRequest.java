package org.example.ktigerstudybe.dto.req;

import lombok.Data;

@Data
public class LessonRequest {
    private String lessonName; // sửa lại đúng camelCase
    private String lessonDescription;
    private Long levelId; // THÊM DÒNG NÀY
}
