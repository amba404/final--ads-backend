package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Данные для регистрации пользователя")
public class Register {

    @NotEmpty
    @Size(min = 4, max = 32)
    @Schema(description = "логин")
    private String username;

    @NotEmpty
    @Size(min = 8, max = 16)
    @Schema(description = "пароль")
    private String password;

    @NotEmpty
    @Size(min = 2, max = 16)
    @Schema(description = "имя пользователя")
    private String firstName;

    @NotEmpty
    @Size(min = 2, max = 16)
    @Schema(description = "фамилия пользователя")
    private String lastName;

    @Pattern(regexp = "^\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}$")
    @Schema(description = "телефон пользователя")
    private String phone;

    @NotNull
    @Schema(description = "роль пользователя")
    private Role role;
}
