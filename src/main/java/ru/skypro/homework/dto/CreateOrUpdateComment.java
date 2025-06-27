package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Данные для создания / обновления комментария")
public class CreateOrUpdateComment {

    @NotEmpty
    @Size(min = 8, max = 64)
    @Schema(description = "текст комментария")
    private String text;
}
