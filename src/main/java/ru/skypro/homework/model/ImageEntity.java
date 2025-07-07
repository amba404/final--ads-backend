package ru.skypro.homework.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

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

    public ImageEntity(UUID uuid) {
        if (uuid == null) {
            this.id = UUID.randomUUID();
        } else {
            this.id = uuid;
        }
    }

    public String getUrl() {
        if (id == null) {
            return null;
        }

        return "/image/" + id.toString();
    }
}
