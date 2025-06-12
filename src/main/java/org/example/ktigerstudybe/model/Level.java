package org.example.ktigerstudybe.model;

import jakarta.persistence.*;
<<<<<<< HEAD
import lombok.Getter;
=======
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
>>>>>>> f7cadc7 (cap nhat api user sign up in)
import lombok.Setter;

import java.util.List;

@Entity
<<<<<<< HEAD
@Getter
@Setter
@Table(name = "level")
public class Level {

=======
@Table(name = "level")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Level {
>>>>>>> f7cadc7 (cap nhat api user sign up in)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LevelID")
    private Long levelId;

    @Column(name = "LevelName")
    private String levelName;

    @Column(name = "Description")
    private String description;
<<<<<<< HEAD

    // Một Level có nhiều Lesson
    @OneToMany(mappedBy = "level", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lesson> lessons;
=======
>>>>>>> f7cadc7 (cap nhat api user sign up in)
}
