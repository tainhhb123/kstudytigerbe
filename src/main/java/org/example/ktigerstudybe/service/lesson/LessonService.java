package org.example.ktigerstudybe.service.lesson;

import org.example.ktigerstudybe.dto.req.LessonRequest;
import org.example.ktigerstudybe.dto.resp.LessonResponse;
import org.example.ktigerstudybe.dto.resp.LessonWithProgressResponse;

import java.util.List;

public interface LessonService {
    LessonResponse createLesson(LessonRequest request);
    LessonResponse updateLesson(Long Id, LessonRequest request);
    List<LessonResponse> getAllLessons();
    LessonResponse getLessonById(Long Id);
    void deleteLesson(Long Id);

    List<LessonResponse> getLessonsByLevelId(Long levelId);

    List<LessonWithProgressResponse> getLessonsWithProgress(Long levelId, Long userId);
}
