package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.ImageEntity;

import java.io.IOException;
import java.util.UUID;

public interface ImageService {
    byte[] saveImage(UUID uuid, MultipartFile file) throws IOException;

    ImageEntity findById(UUID uuid);

}
