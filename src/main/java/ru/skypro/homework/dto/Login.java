package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.skypro.homework.config.UserConfig;

@Data
@Schema(description = "Данные для входа")
public class Login {

    @NotEmpty
    @Size(min = UserConfig.EMAIL_MIN_LENGTH, max = UserConfig.EMAIL_MAX_LENGTH)
    @Email
    @Schema(description = "логин")
    private String username;

    @NotEmpty
    @Size(min = UserConfig.PASSWORD_MIN_LENGTH, max = UserConfig.PASSWORD_MAX_LENGTH)
    @Schema(description = "пароль")
    private String password;
}
