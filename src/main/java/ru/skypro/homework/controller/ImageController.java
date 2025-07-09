package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.model.ImageEntity;
import ru.skypro.homework.service.ImageService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

/**
 * Контроллер для работы с изображениями
 * <p>
 * Обрабатывает запросы на получение изображения по id
 */
@Tag(name = "Изображения", description = "Контроллер для работы с изображениями")
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    /**
     * Получить изображение по id
     * @param id id изображения
     */
    @Operation(summary = "Получить изображение по id", operationId = "getImageById")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "image/*")}),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void getImageById(@PathVariable UUID id, HttpServletResponse response) throws IOException {
        ImageEntity image = imageService.findById(id);

        Path path = Path.of(image.getFilePath());
        try (InputStream is = Files.newInputStream(path);
             OutputStream os = response.getOutputStream()) {
            response.setContentType(image.getMediaType());
            response.setContentLength((int) image.getFileSize());
            is.transferTo(os);
        }
    }
}
