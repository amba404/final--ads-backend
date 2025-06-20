package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;

public interface AdsService {

    Ads getAllAds();

    Ad addAd(CreateOrUpdateAd ad, MultipartFile image);

    ExtendedAd getAds(int id);

    Ad updateAds(int id, CreateOrUpdateAd ad);

    boolean removeAd(int id);

    Ads getAdsMe();

    byte[] updateImage(int id, MultipartFile image);

}
