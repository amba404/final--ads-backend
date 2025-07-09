package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.UserEntity;

import java.util.List;

/**
 * Репозиторий управления сущностями БД для объявлений
 */
@Repository
public interface AdsRepository extends JpaRepository<AdEntity, Integer> {
    /**
     * Поиск всех объявлений по автору
     * @param currentUser пользователь {@link UserEntity}
     * @return список объявлений List {@link AdEntity}
     */
    List<AdEntity> findAllByAuthor(UserEntity currentUser);
}
