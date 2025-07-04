package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
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

@Tag(name = "Комментарии", description = "Методы для работы с комментариями")
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "Получение комментариев объявления", operationId = "getComments")
    @GetMapping("/{id}/comments")
    public Comments getComments(@PathVariable int id) {
        return commentService.getComments(id);
    }

    @Operation(summary = "Добавление комментария к объявлению", operationId = "addComment")
    @PostMapping("/{id}/comments")
    public Comment addComment(Principal principal, @PathVariable int id, @RequestBody @Validated CreateOrUpdateComment comment) {
        return commentService.addComment(principal.getName(), id, comment);
    }

    @Operation(summary = "Удаление комментария", operationId = "deleteComment")
    @DeleteMapping("/{id}/comments/{commentId}")
    public void deleteComment(Principal principal, @PathVariable int id, @PathVariable int commentId) {
        commentService.deleteComment(principal.getName(), id, commentId);
    }

    @Operation(summary = "Обновление комментария", operationId = "updateComment")
    @PatchMapping("/{id}/comments/{commentId}")
    public Comment updateComment(Principal principal, @PathVariable int id, @PathVariable int commentId, @RequestBody @Validated CreateOrUpdateComment comment) {
        return commentService.updateComment(principal.getName(), id, commentId, comment);
    }
}
