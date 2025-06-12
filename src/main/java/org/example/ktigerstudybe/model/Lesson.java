package org.example.ktigerstudybe.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
<<<<<<< HEAD

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "lesson")
=======
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "lesson")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
>>>>>>> f7cadc7 (cap nhat api user sign up in)
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LessonID")
    private Long lessonId;

    @Column(name = "LessonName")
    private String lessonName;

    @Column(name = "LessonDescription")
    private String lessonDescription;

<<<<<<< HEAD
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
=======
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LevelID", referencedColumnName = "LevelID")
    private Level level;
>>>>>>> f7cadc7 (cap nhat api user sign up in)
}
