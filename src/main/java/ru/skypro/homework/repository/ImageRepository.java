package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.ImageEntity;

import java.util.UUID;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, UUID> {
}
