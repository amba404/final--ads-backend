package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.model.AdEntity;

import java.io.IOException;

/**
 * Описание интерфейса сервиса для работы с объявлениями
 */
public interface AdsService {

    Ads getAllAds();

    Ad addAd(String username, CreateOrUpdateAd ad, MultipartFile image) throws IOException;

    ExtendedAd getAds(int id);

    Ad updateAds(String username, int id, CreateOrUpdateAd ad);

    void removeAd(String username, int id);

    Ads getAdsMe(String username);

    byte[] updateImage(String username, int id, MultipartFile image) throws IOException;

    AdEntity getAdOrThrow(int id);
}
