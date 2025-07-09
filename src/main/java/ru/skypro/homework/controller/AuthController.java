package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.service.AuthService;

/**
 * Контроллер для авторизации и регистрации пользователя
 * Обрабатывает HTTP запросы на авторизацию и регистрацию
 */
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Метод для авторизации пользователя
     * @param login объект с данными для авторизации {@link Login}
     * @return Ответ сервера 200 OK или 401 Unauthorized
     */
    @Tag(name = "Авторизация")
    @Operation(summary = "Авторизация пользователя", operationId = "login")
    @PostMapping("/login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<?> login(@RequestBody @Validated Login login) {
        if (authService.login(login.getUsername(), login.getPassword())) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Метод для регистрации нового пользователя
     * @param register объект с данными для регистрации {@link Register}
     * @return Ответ сервера 201 Created или 400 Bad Request
     */
    @Tag(name = "Регистрация")
    @Operation(summary = "Регистрация пользователя", operationId = "register")
    @PostMapping("/register")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    public ResponseEntity<?> register(@RequestBody @Validated Register register) {
        if (authService.register(register)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
