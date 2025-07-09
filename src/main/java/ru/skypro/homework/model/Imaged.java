package ru.skypro.homework.model;

/**
 * Интерфейс для сущностей, которые имеют изображение
 */
public interface Imaged {
    ImageEntity getImage();

    void setImage(ImageEntity image);
}
