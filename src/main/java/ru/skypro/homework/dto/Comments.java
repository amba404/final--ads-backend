package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Schema(description = "Список комментариев")
@Getter
@Setter
public class Comments {

    @Schema(description = "общее количество комментариев")
    private int count;

    @NotNull
    @Schema(description = "Комментарии")
    private List<Comment> results;
}
