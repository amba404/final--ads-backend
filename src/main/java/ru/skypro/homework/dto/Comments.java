package ru.skypro.homework.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class Comments {

    @NotNull
    private Integer count;

    @NotNull
    private List<Comment> results;
}
