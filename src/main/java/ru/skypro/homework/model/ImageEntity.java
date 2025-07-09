package ru.skypro.homework.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

/**
 * Сущность изображения, для хранения в БД
 */
@Entity
@Table(name = "images")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ImageEntity {
    @Id
    private UUID id;

    @NotEmpty(message = "Путь не может быть пустым")
    private String filePath;

    private long fileSize;

    @NotEmpty(message = "Тип файла не может быть пустым")
    private String mediaType;

    /**
     * Конструктор нового объекта, с генерацией UUID или установкой заданного
     * @param uuid UUID для объекта или null для генерации нового UUID
     */
    public ImageEntity(UUID uuid) {
        this.id = Objects.requireNonNullElseGet(uuid, UUID::randomUUID);
    }

    /**
     * Генерирует ссылку на изображение
     */
    public String getUrl() {
        if (id == null) {
            return null;
        }

        return "/image/" + id;
    }
}
