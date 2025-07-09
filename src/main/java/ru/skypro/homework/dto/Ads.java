package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * DTO для списка объявлений
 */
@Data
@Schema(description = "Список объявлений")
@Getter
@Setter
public class Ads {

    @Schema(description = "общее количество объявлений")
    private int count;

    @NotNull
    @Schema(description = "список объявлений")
    private List<Ad> results;
}
