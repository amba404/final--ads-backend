package ru.skypro.homework.service.impl;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.model.ImageEntity;
import ru.skypro.homework.model.Imaged;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.service.ImageService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    @Value(value = "${path.to.images:images}")
    private String imageDirectory;

    @Override
    public byte[] saveImage(Imaged object, @NotNull MultipartFile mFile) throws IOException {

        UUID uuid;

        if (object.getImage() == null) {
            uuid = UUID.randomUUID();
        } else {
            uuid = object.getImage().getId();
        }

        String imageName = uuid.toString();
        Path filePath = Path.of(imageDirectory, imageName + "." + getExtension(mFile.getOriginalFilename()));

        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (InputStream is = mFile.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, StandardOpenOption.CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        }

        ImageEntity image = findImageOrNew(uuid);
        image.setMediaType(mFile.getContentType());
        image.setFileSize(mFile.getSize());
        image.setFilePath(filePath.toString());

        imageRepository.save(image);

        object.setImage(image);

        return mFile.getBytes();
    }

    @Override
    public ImageEntity findById(UUID uuid) {
        return findImageOrFail(uuid);
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private ImageEntity findImageOrNew(UUID uuid) {
        return imageRepository.findById(uuid)
                .orElse(new ImageEntity(uuid));
    }

    private ImageEntity findImageOrFail(UUID uuid) {
        return imageRepository.findById(uuid).orElseThrow(() -> new NotFoundException("Image not found"));
    }
}
