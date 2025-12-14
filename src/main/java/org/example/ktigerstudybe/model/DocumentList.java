package org.example.ktigerstudybe.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "document_list")
public class DocumentList {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "list_id")
        private Long listId;

        // ===== FK tới User =====
        @ManyToOne
        @JoinColumn(name = "user_id", nullable = false)
        private User user;

        @Column(name = "title", nullable = false)
        private String title;

        @Column(name = "description")
        private String description;

        @Column(name = "created_at")
        private LocalDateTime createdAt;

        @Column(name = "is_public")
        private Boolean isPublic = false;

        @PrePersist
        protected void onCreate() {
            this.createdAt = LocalDateTime.now();

    }

}
