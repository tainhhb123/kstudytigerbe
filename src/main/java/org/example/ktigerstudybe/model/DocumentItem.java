package org.example.ktigerstudybe.model;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "document_item")
public class DocumentItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "word_id")
    private Long wordId;

    @ManyToOne
    @JoinColumn(name = "list_id", nullable = false)
    private DocumentList documentList;

    @Column(name = "word", nullable = false)
    private String word;

    @Column(name = "meaning", nullable = false)
    private String meaning;

    @Column(name = "example", columnDefinition = "TEXT")
    private String example;

    @Column(name = "vocab_image")
    private String vocabImage;
}
