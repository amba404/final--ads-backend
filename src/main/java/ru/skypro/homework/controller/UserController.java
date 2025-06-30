package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.service.UserService;

import java.io.IOException;
import java.security.Principal;

@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "Пользователи", description = "Управление данными пользователей")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Обновление пароля", operationId = "setPassword")
    @PostMapping("/set_password")
    public void setPassword(Principal principal, @RequestBody @Validated NewPassword newPassword) {
        userService.setPassword(principal.getName(), newPassword);
    }

    @Operation(summary = "Получение информации об авторизованном пользователе", operationId = "getCurrentUser")
    @GetMapping("/me")
    public User getCurrentUser(Principal principal) {
        return userService.getUser(principal.getName());
    }

    @Operation(summary = "Обновление информации об авторизованном пользователе", operationId = "updateUser")
    @PatchMapping("/me")
    public UpdateUser updateUser(Principal principal, @RequestBody @Validated UpdateUser newUser) {
        return userService.updateUser(principal.getName(), newUser);
    }

    @Operation(summary = "Обновление аватара авторизованного пользователя", operationId = "updateUserImage")
    @PatchMapping(path = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateUserImage(Principal principal, @RequestParam MultipartFile image) throws IOException {
        byte[] bytes = userService.updateUserImage(principal.getName(), image);
        if (bytes.length > 0) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
