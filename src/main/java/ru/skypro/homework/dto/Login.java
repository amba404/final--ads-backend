package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Данные для входа")
public class Login {

    @NotEmpty
    @Size(min = 4, max = 32)
    @Schema(description = "логин")
    private String username;

    @NotEmpty
    @Size(min = 8, max = 16)
    @Schema(description = "пароль")
    private String password;
}
