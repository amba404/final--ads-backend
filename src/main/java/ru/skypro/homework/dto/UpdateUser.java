package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.skypro.homework.config.UserConfig;

/**
 * DTO для обновления данных пользователя
 */
@Data
@Schema(description = "Данные для обновления данных пользователя")
public class UpdateUser {

    @Size(min = UserConfig.FIRST_NAME_MIN_LENGTH, max = UserConfig.FIRST_NAME_MAX_LENGTH)
    @Schema(description = "имя пользователя")
    private String firstName;

    @Size(min = UserConfig.LAST_NAME_MIN_LENGTH, max = UserConfig.LAST_NAME_MAX_LENGTH)
    @Schema(description = "фамилия пользователя")
    private String lastName;

    @Pattern(regexp = UserConfig.PHONE_PATTERN)
    @Schema(description = "телефон пользователя")
    private String phone;

}
