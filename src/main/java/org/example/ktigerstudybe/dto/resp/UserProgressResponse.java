package org.example.ktigerstudybe.dto.resp;

import lombok.Data;
import java.time.LocalDate;

@Data
public class UserProgressResponse {
    private Long userId;
    private String avatarImage;
    private String fullName;
    private LocalDate joinDate;
    private String levelName;
    private String lessonName;
}
