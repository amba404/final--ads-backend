package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.skypro.homework.config.UserConfig;

/**
 * DTO для изменения пароля
 */
@Schema(description = "Изменение пароля")
@Data
public class NewPassword {

    @NotEmpty
    @Size(min = UserConfig.PASSWORD_MIN_LENGTH, max = UserConfig.PASSWORD_MAX_LENGTH)
    @Schema(description = "текущий пароль", minLength = UserConfig.PASSWORD_MIN_LENGTH, maxLength = UserConfig.PASSWORD_MAX_LENGTH)
    private String currentPassword;

    @NotEmpty
    @Schema(description = "новый пароль", minLength = UserConfig.PASSWORD_MIN_LENGTH, maxLength = UserConfig.PASSWORD_MAX_LENGTH)
    @Size(min = UserConfig.PASSWORD_MIN_LENGTH, max = UserConfig.PASSWORD_MAX_LENGTH)
    private String newPassword;
}
