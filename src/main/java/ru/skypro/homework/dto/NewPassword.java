package ru.skypro.homework.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NewPassword {

    @NotEmpty
    @Size(min = 8, max = 16)
    private String currentPassword;

    @NotEmpty
    @Size(min = 8, max = 16)
    private String newPassword;
}
