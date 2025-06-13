package org.example.ktigerstudybe.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;



import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "lesson")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
    @OneToMany(mappedBy = "lesson")
    private List<VocabularyTheory> vocabularies;
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
    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    private List<VocabularyTheory> vocabularies;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    private List<GrammarTheory> grammars;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    private List<Exercise> exercises;

>>>>>>> 0c7c1bc9c63b90284bc17f082b106f324534e597
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LevelID", referencedColumnName = "LevelID")
    private Level level;
}
