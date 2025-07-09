package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.exception.UnauthorizedException;
import ru.skypro.homework.service.AdsService;

import java.io.IOException;
import java.security.Principal;

/**
 * Контроллер для работы с объявлениями
 * <p>
 * Обрабатывает HTTP-запросы, связанные с объявлениями.
 */
@Tag(name = "Объявления", description = "Методы для работы с объявлениями")
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
public class AdsController {

    private final AdsService adsService;

    /**
     * Получение всех объявлений
     * <p>
     * Возвращает все объявления, всех пользователей, без авторизации
     * @return объект {@link Ads} с массивом объявлений
     */
    @Operation(summary = "Получение всех объявлений", operationId = "getAllAds")
    @GetMapping()
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = Ads.class))})
    })
    public Ads getAllAds() {
        return adsService.getAllAds();
    }

    /**
     * Добавление объявления авторизованным пользователем
     * @param principal авторизованный пользователь {@link Principal}
     * @param properties свойства добавляемого объявления {@link CreateOrUpdateAd}
     * @param image файл изображения для объявления {@link MultipartFile}
     * @return объект {@link Ad} с добавленным объявлением
     */
    @Operation(summary = "Добавление объявления", operationId = "addAd")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Secured({"USER", "ADMIN"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = Ad.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public Ad addAd(Principal principal, @RequestPart("properties") @Validated CreateOrUpdateAd properties, @RequestPart("image") MultipartFile image) throws IOException, UnauthorizedException {
        if (principal == null) {
            throw new UnauthorizedException("User is not authorized");
        }
        return adsService.addAd(principal.getName(), properties, image);
    }

    /**
     * Получение информации об объявлении по id
     * @param id id объявления
     * @return объект {@link ExtendedAd} с информацией об объявлении
     */
    @Operation(summary = "Получение информации об объявлении", operationId = "getAds")
    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = ExtendedAd.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ExtendedAd getAds(@PathVariable int id) {
        return adsService.getAds(id);
    }

    /**
     * Удаление объявления авторизованным пользователем
     * @param principal авторизованный пользователь {@link Principal}
     * @param id id объявления
     */
    @Operation(summary = "Удаление объявления", operationId = "removeAd")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public void removeAd(Principal principal, @PathVariable int id) {
        adsService.removeAd(principal.getName(), id);
    }

    /**
     * Обновление информации об объявлении авторизованным пользователем
     * @param principal авторизованный пользователь {@link Principal}
     * @param id id объявления
     * @param updateAd обновленные свойства объявления {@link CreateOrUpdateAd}
     * @return объект {@link Ad} со свойствами объявления
     */
    @Operation(summary = "Обновление информации об объявлении", operationId = "updateAds")
    @PatchMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = Ad.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public Ad updateAds(Principal principal, @PathVariable int id, @RequestBody @Validated CreateOrUpdateAd updateAd) {
        return adsService.updateAds(principal.getName(), id, updateAd);
    }

    /**
     * Получение объявлений авторизованного пользователя
     * @param principal авторизованный пользователь {@link Principal}
     * @return объект {@link Ads} с массивом объявлений
     */
    @Operation(summary = "Получение объявлений авторизованного пользователя", operationId = "getAdsMe")
    @GetMapping("/me")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = Ads.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public Ads getAdsMe(Principal principal) {
        return adsService.getAdsMe(principal.getName());
    }

    /**
     * Обновление картинки объявления
     * @param principal авторизованный пользователь {@link Principal}
     * @param id id объявления
     * @param image файл изображения для объявления {@link MultipartFile}
     * @return массив байт картинки в теле ответа
     */
    @Operation(summary = "Обновление картинки объявления", operationId = "updateImage")
    @PatchMapping("/{id}/image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content =
                    {@Content(mediaType = "application/json", schema = @Schema(implementation = String[].class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<?> updateImage(Principal principal, @PathVariable int id, @RequestBody MultipartFile image) throws IOException {
        byte[] bytes = adsService.updateImage(principal.getName(), id, image);
        if (bytes.length > 0) {
            return ResponseEntity.ok()
                    .body(bytes);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
