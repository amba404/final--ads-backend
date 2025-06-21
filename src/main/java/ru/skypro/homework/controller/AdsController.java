package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.service.AdsService;

@Tag(name = "Объявления", description = "Методы для работы с объявлениями")
@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
public class AdsController {

    private final AdsService adsService;

    @Operation(summary = "Получение всех объявлений", operationId = "getAllAds")
    @GetMapping()
    public Ads getAllAds() {
        return adsService.getAllAds();
    }

    @Operation(summary = "Добавление объявления", operationId = "addAd")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Ad addAd(@RequestBody CreateOrUpdateAd properties, @RequestBody MultipartFile image) {
        return adsService.addAd(properties, image);
    }

    @Operation(summary = "Получение информации об объявлении", operationId = "getAds")
    @GetMapping("/{id}")
    public ExtendedAd getAds(@PathVariable int id) {
        return adsService.getAds(id);
    }

    @Operation(summary = "Удаление объявления", operationId = "removeAd")
    @DeleteMapping("/{id}")
    public void removeAd(@PathVariable int id) {
        adsService.removeAd(id);
    }

    @Operation(summary = "Обновление информации об объявлении", operationId = "updateAds")
    @PatchMapping("/{id}")
    public Ad updateAds(@PathVariable int id, @RequestBody CreateOrUpdateAd updateAd) {
        return adsService.updateAds(id, updateAd);
    }

    @Operation(summary = "Получение объявлений авторизованного пользователя", operationId = "getAdsMe")
    @GetMapping("/me")
    public Ads getAdsMe() {
        return adsService.getAdsMe();
    }

    @Operation(summary = "Обновление картинки объявления", operationId = "updateImage")
    @PatchMapping("/{id}/image")
    public ResponseEntity<?> updateImage(@PathVariable int id, @RequestBody MultipartFile image) {
        byte[] bytes = adsService.updateImage(id, image);
        if (bytes.length > 0) {
            return ResponseEntity.ok()
                    .body(bytes);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
