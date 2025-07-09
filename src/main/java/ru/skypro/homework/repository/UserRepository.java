package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.UserEntity;

import java.util.Optional;

/**
 * Репозиторий управления сущностями БД для пользователей
 */

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    /**
     * Поиск пользователя по имени. Необходим согласно контракту UserServiceDetails
     * @param username имя пользователя
     * @return пользователь, если найден, иначе null
     */
    Optional<UserEntity> findByUsername(String username);

    /**
     * Проверка существования пользователя по имени
     * @param userName имя пользователя
     * @return true, если пользователь существует, иначе false
     */
    boolean existsByUsername(String userName);
}
