package org.example.ktigerstudybe.service.lesson;

import org.example.ktigerstudybe.dto.req.LessonRequest;
import org.example.ktigerstudybe.dto.resp.LessonResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface LessonService {
    LessonResponse createLesson(LessonRequest request);
    LessonResponse updateLesson(Long Id, LessonRequest request);
    List<LessonResponse> getAllLessons();
    LessonResponse getLessonById(Long Id);
    void deleteLesson(Long Id);

    List<LessonResponse> getLessonsByLevelId(Long levelId);

    //Admin
    // NEW: API phân trang, tìm kiếm
    Page<LessonResponse> getLessons(
            int page,
            int size,
            Long levelId,
            String keyword
    );
}
