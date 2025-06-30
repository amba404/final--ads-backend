package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import ru.skypro.homework.config.UserConfig;

@Data
@Schema(description = "Данные для регистрации пользователя")
public class Register {

    @NotEmpty
    @Size(min = UserConfig.EMAIL_MIN_LENGTH, max = UserConfig.EMAIL_MAX_LENGTH)
    @Schema(description = "логин")
    @Email
    private String username;

    @NotEmpty
    @Size(min = UserConfig.PASSWORD_MIN_LENGTH, max = UserConfig.PASSWORD_MAX_LENGTH)
    @Schema(description = "пароль")
    private String password;

    @NotEmpty
    @Size(min = UserConfig.FIRST_NAME_MIN_LENGTH, max = UserConfig.FIRST_NAME_MAX_LENGTH)
    @Schema(description = "имя пользователя")
    private String firstName;

    @NotEmpty
    @Size(min = UserConfig.LAST_NAME_MIN_LENGTH, max = UserConfig.LAST_NAME_MAX_LENGTH)
    @Schema(description = "фамилия пользователя")
    private String lastName;

    @Pattern(regexp = UserConfig.PHONE_PATTERN)
    @Schema(description = "телефон пользователя")
    private String phone;

    @NotNull
    @Schema(description = "роль пользователя")
    private Role role;
}
