package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserService;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdsServiceImpl implements AdsService {

    private final ImageService imageService;
    private final UserService userService;
    private final AdsRepository adsRepository;

    @Override
    public Ads getAllAds() {
        return new Ads();
    }

    @Override
    public Ad addAd(CreateOrUpdateAd ad, MultipartFile image) throws IOException {
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
    public byte[] updateImage(String username, int id, MultipartFile image) throws IOException {
        UserEntity userEntity = userService.getUserOrThrow(username);
        AdEntity adEntity = adsRepository.findById(id).orElseThrow(() -> new NotFoundException("Ad not found"));

        UUID imageId;
        if (adEntity.getImage() == null) {
            imageId = UUID.randomUUID();
        } else {
            imageId = adEntity.getImage().getId();
        }

        byte[] bytes = imageService.saveImage(imageId, image);

        adEntity.setImage(imageService.findById(imageId));
        adsRepository.save(adEntity);

        return bytes;
    }
}
