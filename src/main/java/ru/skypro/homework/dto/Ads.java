package ru.skypro.homework.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class Ads {

    @NotNull
    private Integer count;

    @NotNull
    private List<Ad> results;
}
