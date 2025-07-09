package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.exception.NoRightsException;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserService;

import java.io.IOException;
import java.util.List;

/**
 * Реализация сервиса для работы с объявлениями
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AdsServiceImpl implements AdsService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ImageService imageService;
    private final UserService userService;
    private final AdsRepository adsRepository;
    private final AdMapper adMapper;

    /**
     * Метод для получения всех объявлений
     * @return DTO {@link Ads}
     */
    @Override
    public Ads getAllAds() {
        return adMapper.toAdsDto(adsRepository.findAll());
    }

    /**
     * Метод для добавления объявления
     * @param username имя пользователя
     * @param ad DTO {@link CreateOrUpdateAd}
     * @param image файл изображения {@link MultipartFile}
     * @return DTO {@link Ad}
     */
    @Override
    public Ad addAd(String username, CreateOrUpdateAd ad, MultipartFile image) throws IOException {
        UserEntity userEntity = userService.getUserOrThrow(username);

        AdEntity adEntity = adMapper.toAdEntity(ad);

        adEntity.setAuthor(userEntity);

        imageService.saveImage(adEntity, image);

        return adMapper.toAdDto(adsRepository.save(adEntity));
    }

    /**
     * Метод для получения объявления по id
     * @param id id объявления
     * @return DTO {@link ExtendedAd}
     */
    @Override
    public ExtendedAd getAds(int id) {
        return adMapper.toExtendedAdDto(getAdOrThrow(id));
    }

    /**
     * Метод для обновления объявления.
     * Если пользователь не является автором объявления и не админ, то выбрасывается исключение
     * @param username имя пользователя
     * @param id id объявления
     * @param ad информация для обновления объявления {@link CreateOrUpdateAd}
     * @return DTO {@link Ad}
     * @throws NotFoundException если объявление не найдено по id
     * @throws NoRightsException если пользователь не является автором объявления и не админ
     */
    @Override
    public Ad updateAds(String username, int id, CreateOrUpdateAd ad) {
        AdEntity adEntity = getAdOrThrow(id);

        userService.checkOwnerOrThrow(username, adEntity.getAuthor());

        adEntity.setTitle(ad.getTitle());
        adEntity.setDescription(ad.getDescription());
        adEntity.setPrice(ad.getPrice());

        return adMapper.toAdDto(adsRepository.save(adEntity));
    }

    /**
     * Метод для удаления объявления.
     * Если пользователь не является автором объявления и не админ, то выбрасывается исключение
     * @param username имя пользователя
     * @param id id объявления
     * @throws NotFoundException если объявление не найдено по id
     * @throws NoRightsException если пользователь не является автором объявления и не админ
     */
    @Override
    public void removeAd(String username, int id) {
        AdEntity adEntity = getAdOrThrow(id);

        userService.checkOwnerOrThrow(username, adEntity.getAuthor());

        try {
            imageService.deleteImage(adEntity);
        } catch (IOException e) {
            logger.error("Error while deleting image", e);
            return;
        }

        adsRepository.delete(adEntity);
    }

    /**
     * Метод для получения объявлений пользователя
     * @param username имя пользователя
     * @return DTO {@link Ads}
     */
    @Override
    public Ads getAdsMe(String username) {
        UserEntity currentUser = userService.getUserOrThrow(username);

        List<AdEntity> ads = adsRepository.findAllByAuthor(currentUser);

        return adMapper.toAdsDto(ads);
    }

    /**
     * Метод для обновления изображения объявления
     * Если пользователь не является автором объявления и не админ, то выбрасывается исключение
     * @param username имя пользователя
     * @param id id объявления
     * @param image файл изображения {@link MultipartFile}
     * @return массив байт изображения
     * @throws NotFoundException если объявление не найдено по id
     * @throws NoRightsException если пользователь не является автором объявления и не админ
     */
    @Override
    public byte[] updateImage(String username, int id, MultipartFile image) throws IOException {
        AdEntity adEntity = getAdOrThrow(id);

        userService.checkOwnerOrThrow(username, adEntity.getAuthor());

        byte[] bytes = imageService.saveImage(adEntity, image);

        adsRepository.save(adEntity);

        return bytes;

    }

    /**
     * Метод для получения объявления по id
     * @param id id объявления
     * @return {@link AdEntity}
     * @throws NotFoundException если объявление не найдено
     */
    @Override
    public AdEntity getAdOrThrow(int id) {
        return adsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ad not found"));
    }
}
