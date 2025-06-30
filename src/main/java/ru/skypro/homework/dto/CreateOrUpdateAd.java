package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.skypro.homework.config.AdConfig;

@Data
@Schema(description = "Данные для создания или изменения объявления")
public class CreateOrUpdateAd {

    @NotEmpty
    @Size(min = AdConfig.TITLE_MIN_LENGTH, max = AdConfig.TITLE_MAX_LENGTH)
    @Schema(description = "Заголовок объявления")
    private String title;

    @Min(AdConfig.PRICE_MIN)
    @Max(AdConfig.PRICE_MAX)
    @Schema(description = "цена объявления")
    private int price;

    @NotEmpty
    @Size(min = AdConfig.DESCRIPTION_MIN_LENGTH, max = AdConfig.DESCRIPTION_MAX_LENGTH)
    @Schema(description = "описание объявления")
    private String description;

}
