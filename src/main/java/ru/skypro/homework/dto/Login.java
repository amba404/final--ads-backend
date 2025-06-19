package ru.skypro.homework.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Login {

    @NotEmpty
    @Size(min = 4, max = 32)
    private String username;

    @NotEmpty
    @Size(min = 8, max = 16)
    private String password;
}
