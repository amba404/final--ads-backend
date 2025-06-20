package ru.skypro.homework.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateOrUpdateAd {

    @NotEmpty
    @Size(min = 4, max = 32)
    private String title;

    @NotNull
    @Min(1) @Max(10000000)
    private Integer price;

    @NotEmpty
    @Size(min = 8, max = 64)
    private String description;

}
