package ru.skypro.homework.controller;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.model.ImageEntity;
import ru.skypro.homework.service.ImageService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.mockito.Mockito.*;

class ImageControllerTest {

    @Mock
    private ImageService imageService;

    @Mock
    private HttpServletResponse response;

    @Mock
    private InputStream inputStream;

    @Mock
    private ServletOutputStream outputStream;

    @InjectMocks
    private ImageController imageController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetImageById_Success() throws IOException {
        // Arrange
        UUID id = UUID.randomUUID();
        ImageEntity image = new ImageEntity();
        image.setFilePath("/path/to/image.jpg");
        image.setMediaType("image/jpeg");
        image.setFileSize(1024L);

        when(imageService.findById(id)).thenReturn(image);
        when(response.getOutputStream()).thenReturn(outputStream);

        // Mock the Path and Files behavior
        try (MockedStatic<Files> filesMockedStatic = mockStatic(Files.class)) {
            filesMockedStatic.when(() -> Files.newInputStream(any(Path.class))).thenReturn(inputStream);

            // Act
            imageController.getImageById(id, response);

            // Assert
            verify(response).setContentType("image/jpeg");
            verify(response).setContentLength(1024);
            verify(inputStream).transferTo(outputStream);
            verify(response).getOutputStream();
        }
    }

    @Test
    void testGetImageById_ImageNotFound() throws IOException {
        UUID id = UUID.randomUUID();
        when(imageService.findById(id)).thenThrow(new NotFoundException("Image not found"));

        try {
            imageController.getImageById(id, response);
        } catch (NotFoundException e) {
            verifyNoInteractions(response);
        }
    }
}
