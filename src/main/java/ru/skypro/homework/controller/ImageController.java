package ru.skypro.homework.controller;

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

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

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
