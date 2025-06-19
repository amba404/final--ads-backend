package ru.skypro.homework.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Register {

    @NotEmpty
    @Size(min = 4, max = 32)
    private String username;

    @NotEmpty
    @Size(min = 8, max = 16)
    private String password;

    @NotEmpty
    @Size(min = 2, max = 16)
    private String firstName;

    @NotEmpty
    @Size(min = 2, max = 16)
    private String lastName;

    @Pattern(regexp = "^\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}$")
    private String phone;

    @NotNull
    private Role role;
}
