package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.UserEntity;

import java.util.List;

@Repository
public interface AdsRepository extends JpaRepository<AdEntity, Integer> {
    List<AdEntity> findAllByAuthor(UserEntity currentUser);
}
