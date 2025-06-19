package ru.skypro.homework.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Comment {

    @NotNull
    private Integer author;

    private String authorImage;

    @NotEmpty
    private String authorFirstName;

    @NotNull
    private Long createdAt;

    @NotNull
    private Integer pk;

    @NotEmpty
    private String text;
}
