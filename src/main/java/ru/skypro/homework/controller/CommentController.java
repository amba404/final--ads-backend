package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.service.CommentService;

import java.security.Principal;

/**
 * Контроллер для работы с комментариями
 * <p>
 * Обрабатывает HTTP запросы по работе с комментариями
 */
@Tag(name = "Комментарии", description = "Методы для работы с комментариями")
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
public class CommentController {

    private final CommentService commentService;

    /**
     * Получение комментариев объявления
     * @param id id объявления
     * @return объект {@link Comments} с массивом комментариев
     */
    @Operation(summary = "Получение комментариев объявления", operationId = "getComments")
    @GetMapping("/{id}/comments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = Comments.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public Comments getComments(@PathVariable int id) {
        return commentService.getComments(id);
    }

    /**
     * Добавление комментария к объявлению (авторизованным пользователям)
     * @param principal авторизованный пользователь {@link Principal}
     * @param id id объявления
     * @param comment объект свойств добавляемого комментария {@link CreateOrUpdateComment}
     * @return объект {@link Comment} добавленного комментария
     */
    @Operation(summary = "Добавление комментария к объявлению", operationId = "addComment")
    @PostMapping("/{id}/comments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = Comment.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public Comment addComment(Principal principal, @PathVariable int id, @RequestBody @Validated CreateOrUpdateComment comment) {
        return commentService.addComment(principal.getName(), id, comment);
    }

    /**
     * Удаление комментария (авторизованным пользователям)
     * @param principal авторизованный пользователь {@link Principal}
     * @param id id объявления
     * @param commentId id комментария
     */
    @Operation(summary = "Удаление комментария", operationId = "deleteComment")
    @DeleteMapping("/{id}/comments/{commentId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public void deleteComment(Principal principal, @PathVariable int id, @PathVariable int commentId) {
        commentService.deleteComment(principal.getName(), id, commentId);
    }

    /**
     * Обновление комментария (авторизованным пользователям)
     * @param principal авторизованный пользователь {@link Principal}
     * @param id id объявления
     * @param commentId id комментария
     * @param comment объект свойств обновляемого комментария {@link CreateOrUpdateComment}
     * @return объект {@link Comment} обновленного комментария
     */
    @Operation(summary = "Обновление комментария", operationId = "updateComment")
    @PatchMapping("/{id}/comments/{commentId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = Comment.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public Comment updateComment(Principal principal, @PathVariable int id, @PathVariable int commentId, @RequestBody @Validated CreateOrUpdateComment comment) {
        return commentService.updateComment(principal.getName(), id, commentId, comment);
    }
}
