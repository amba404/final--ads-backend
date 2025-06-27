package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Список объявлений")
public class Ads {

    @Schema(description = "общее количество объявлений")
    private int count;

    @NotNull
    @Schema(description = "список объявлений")
    private List<Ad> results;
}
