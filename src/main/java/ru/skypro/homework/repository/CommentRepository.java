package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.CommentEntity;

import java.util.List;

/**
 * Репозиторий управления сущностями БД для комментариев
 */
@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
    /**
     * Поиск всех комментариев по id объявления
     * @param adId id объявления
     * @return список комментариев List {@link CommentEntity}
     */
    List<CommentEntity> findAllByAdId(int adId);

    /**
     * Проверка существования комментария по id объявления
     * @param adId id объявления
     * @return true - если комментарий существует, false - если нет
     */
    boolean existsByAdId(int adId);
}
