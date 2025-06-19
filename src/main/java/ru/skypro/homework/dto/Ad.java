package ru.skypro.homework.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Ad {
    @NotNull
    private Integer author;

    private String image;

    @NotNull
    private Integer pk;

    @NotNull
    private Integer price;

    @NotNull
    private String title;
}