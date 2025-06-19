package ru.skypro.homework.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class User {

    private Integer id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;

    @NotNull
    private Role role;
    private String image;

}
