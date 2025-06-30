package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.exception.NoRightsException;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserService;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdsServiceImpl implements AdsService {

    private final ImageService imageService;
    private final UserService userService;
    private final AdsRepository adsRepository;
    private final AdMapper adMapper;

    @Override
    public Ads getAllAds() {
        return adMapper.toAdsDto(adsRepository.findAll());
    }

    @Override
    public Ad addAd(String username, CreateOrUpdateAd ad, MultipartFile image) throws IOException {
        UserEntity userEntity = userService.getUserOrThrow(username);
        UUID imageId = UUID.randomUUID();

        imageService.saveImage(imageId, image);

        AdEntity adEntity = adMapper.toAdEntity(ad);
        adEntity.setAuthor(userEntity);
        adEntity.setImage(imageService.findById(imageId));

        return adMapper.toAdDto(adsRepository.save(adEntity));
    }

    @Override
    public ExtendedAd getAds(int id) {
        return adMapper.toExtendedAdDto(adsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ad not found")));
    }

    @Override
    public Ad updateAds(String username, int id, CreateOrUpdateAd ad) {
        UserEntity currentUser = userService.getUserOrThrow(username);
        AdEntity adEntity = adsRepository.findById(id).orElseThrow(() -> new NotFoundException("Ad not found"));

        if (currentUser.equals(adEntity.getAuthor()) || currentUser.getRole().name().equals("ADMIN")) {
            adEntity.setTitle(ad.getTitle());
            adEntity.setDescription(ad.getDescription());
            adEntity.setPrice(ad.getPrice());

            return adMapper.toAdDto(adsRepository.save(adEntity));
        } else {
            throw new NoRightsException("You can't update this ad");
        }
    }

    @Override
    public void removeAd(String username, int id) {
        UserEntity currentUser = userService.getUserOrThrow(username);
        AdEntity adEntity = adsRepository.findById(id).orElseThrow(() -> new NotFoundException("Ad not found"));

        if (currentUser.equals(adEntity.getAuthor()) || currentUser.getRole().name().equals("ADMIN")) {
            adsRepository.delete(adEntity);
        } else {
            throw new NoRightsException("You can't delete this ad");
        }
    }

    @Override
    public Ads getAdsMe(String username) {
        UserEntity currentUser = userService.getUserOrThrow(username);

        List<AdEntity> ads = adsRepository.findAllByAuthor(currentUser);

        return adMapper.toAdsDto(ads);
    }

    @Override
    public byte[] updateImage(String username, int id, MultipartFile image) throws IOException {
        UserEntity currentUser = userService.getUserOrThrow(username);
        AdEntity adEntity = adsRepository.findById(id).orElseThrow(() -> new NotFoundException("Ad not found"));

        if (currentUser.equals(adEntity.getAuthor()) || currentUser.getRole().name().equals("ADMIN")) {
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
        } else {
            throw new NoRightsException("You can't update this ad's image");
        }
    }

    @Override
    public AdEntity getAdOrThrow(int id) {
        return adsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ad not found"));
    }
}
