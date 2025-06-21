package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(description = "Изменение пароля")
@Data
public class NewPassword {

    @NotEmpty
    @Size(min = 8, max = 16)
    @Schema(description = "текущий пароль", minLength = 8, maxLength = 16)
    private String currentPassword;

    @NotEmpty
    @Schema(description = "новый пароль", minLength = 8, maxLength = 16)
    @Size(min = 8, max = 16)
    private String newPassword;
}
