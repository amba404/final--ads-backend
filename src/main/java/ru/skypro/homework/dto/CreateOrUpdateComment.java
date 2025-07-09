package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.skypro.homework.config.CommentConfig;

/**
 * DTO для создания или обновления комментария
 */
@Data
@Schema(description = "Данные для создания / обновления комментария")
public class CreateOrUpdateComment {

    @NotEmpty
    @Size(min = CommentConfig.TEXT_MIN_LENGTH, max = CommentConfig.TEXT_MAX_LENGTH)
    @Schema(description = "текст комментария")
    private String text;
}
