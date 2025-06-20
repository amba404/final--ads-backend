package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.service.AdsService;

@Service
public class AdsServiceImpl implements AdsService {
    @Override
    public Ads getAllAds() {
        return null;
    }

    @Override
    public Ad addAd(CreateOrUpdateAd ad, MultipartFile image) {
        return null;
    }

    @Override
    public ExtendedAd getAds(int id) {
        return null;
    }

    @Override
    public Ad updateAds(int id, CreateOrUpdateAd ad) {
        return null;
    }

    @Override
    public boolean removeAd(int id) {
        return false;
    }

    @Override
    public Ads getAdsMe() {
        return null;
    }

    @Override
    public byte[] updateImage(int id, MultipartFile image) {
        return new byte[0];
    }
}
