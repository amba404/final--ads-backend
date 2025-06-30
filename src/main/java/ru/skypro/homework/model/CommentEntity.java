package ru.skypro.homework.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.skypro.homework.config.CommentConfig;

@Entity
@Table(name = "comments")
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Long createdAt;

    @Column(nullable = false, length = CommentConfig.TEXT_MAX_LENGTH)
    private String text;

    @ManyToOne
    private UserEntity author;

    @ManyToOne
    private AdEntity ad;

}
