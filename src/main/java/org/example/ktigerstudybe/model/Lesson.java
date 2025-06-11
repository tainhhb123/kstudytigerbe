package org.example.ktigerstudybe.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "lesson")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LessonID")
    private Long lessonId;

    @Column(name = "LessonName")
    private String lessonName;

    @Column(name = "LessonDescription")
    private String lessonDescription;

    @ManyToOne
    @JoinColumn(name = "LevelID")
    private Level level;

    // Các quan hệ giữ nguyên
//    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
//    private List<VocabularyTheory> vocabularies;
//
//    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
//    private List<GrammarTheory> grammars;
//
//    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
//    private List<Exercise> exercises;
}
