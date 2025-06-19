package ru.skypro.homework.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class User {

    @NotNull
    private Integer id;

    @NotEmpty
    private String email;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotEmpty
    private String phone;

    @NotNull
    private Role role;

    private String image;

}
