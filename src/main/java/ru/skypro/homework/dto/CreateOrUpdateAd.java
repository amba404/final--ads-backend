package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Данные для создания или изменения объявления")
public class CreateOrUpdateAd {

    @NotEmpty
    @Size(min = 4, max = 32)
    @Schema(description = "Заголовок объявления")
    private String title;

    @Min(1) @Max(10000000)
    @Schema(description = "цена объявления")
    private int price;

    @NotEmpty
    @Size(min = 8, max = 64)
    @Schema(description = "описание объявления")
    private String description;

}
