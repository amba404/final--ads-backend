package ru.skypro.homework.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import ru.skypro.homework.config.AdConfig;

@Entity
@Table(name = "ads")
@Getter
@Setter
public class AdEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

}
