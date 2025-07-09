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

/**
 * Реализация сервиса для работы с изображениями
 */
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    @Value(value = "${path.to.images:images}")
    private String imageDirectory;

    /**
     * Сохранение изображения.
     * <p>
     * Сохраняет изображение в директорию. Создает запись в БД. Устанавливает связь изображения с объектом.
     * Если объект уже имеет изображение, то оно будет перезаписано.
     * @param object объект, для которого сохраняется изображение
     * @param mFile файл с изображением {@link MultipartFile}
     * @return массив байтов изображения
     */
    @Override
    public byte[] saveImage(Imaged object, @NotNull MultipartFile mFile) throws IOException {

        UUID uuid;

        if (object.getImage() == null) {
            uuid = UUID.randomUUID();
        } else {
            uuid = object.getImage().getId();
        }

        String imageName = uuid.toString() + "." + getExtension(mFile.getOriginalFilename());

        Path filePath = Path.of(imageDirectory, imageName);

        Files.createDirectories(filePath.getParent());

        deleteFile(imageName);

        try (InputStream is = mFile.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, StandardOpenOption.CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        }

        ImageEntity image = findImageOrNew(uuid);

        if (image.getFilePath() != null) {
            deleteFile(image.getFilePath());
        }

        image.setMediaType(mFile.getContentType());
        image.setFileSize(mFile.getSize());
        image.setFilePath(filePath.toString());

        imageRepository.save(image);
        imageRepository.flush();

        object.setImage(image);

        return mFile.getBytes();
    }

    /**
     * Получение изображения по UUID
     * @param uuid UUID изображения
     * @return массив байтов изображения
     * @throws NotFoundException если изображение не найдено
     */
    @Override
    public ImageEntity findById(UUID uuid) {
        return findImageOrFail(uuid);
    }

    /**
     * Удаление изображения.
     * <p>
     * Очищает поле изображения у объекта. Удаляет файл из директории. Удаляет запись в БД
     * @param object объект, у которого удаляется изображение
     */
    @Override
    public void deleteImage(Imaged object) throws IOException {
        if (object.getImage() == null) {
            return;
        }

        ImageEntity image = object.getImage();

        if (image == null) {
            return;
        }

        try {
            deleteFile(image.getFilePath());
        } catch (IOException e) {
            throw new IOException(e);
        }

        object.setImage(null);

        imageRepository.delete(image);
    }

    private void deleteFile(@NotNull String imageName) throws IOException {
        Path filePath = Path.of(imageDirectory, imageName);

        Files.deleteIfExists(filePath);
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
