package ru.skypro.homework.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateOrUpdateComment {

    @NotEmpty
    @Size(min = 8, max = 64)
    private String text;
}
