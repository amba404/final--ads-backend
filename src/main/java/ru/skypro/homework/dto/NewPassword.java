package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.skypro.homework.config.UserConfig;

@Schema(description = "Изменение пароля")
@Data
public class NewPassword {

    @NotEmpty
    @Size(min = UserConfig.PASSWORD_MIN_LENGTH, max = UserConfig.PASSWORD_MAX_LENGTH)
    @Schema(description = "текущий пароль", minLength = 8, maxLength = 16)
    private String currentPassword;

    @NotEmpty
    @Schema(description = "новый пароль", minLength = 8, maxLength = 16)
    @Size(min = UserConfig.PASSWORD_MIN_LENGTH, max = UserConfig.PASSWORD_MAX_LENGTH)
    private String newPassword;
}
