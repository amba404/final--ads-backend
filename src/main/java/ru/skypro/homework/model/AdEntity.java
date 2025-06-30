package ru.skypro.homework.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.skypro.homework.config.AdConfig;

import java.util.List;

@Entity
@Table(name = "ads")
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AdEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @Column(length = AdConfig.TITLE_MAX_LENGTH, nullable = false)
    private String title;

    @Column(nullable = false)
    @Min(AdConfig.PRICE_MIN)
    @Max(AdConfig.PRICE_MAX)
    private int price;

    @Column(length = AdConfig.DESCRIPTION_MAX_LENGTH, nullable = false)
    private String description;

    @ManyToOne
    private UserEntity author;

    @ManyToOne
    private ImageEntity image;

    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CommentEntity> comments;

}
