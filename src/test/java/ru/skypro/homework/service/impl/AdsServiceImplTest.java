package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdsServiceImplTest {

    @InjectMocks
    private AdsServiceImpl adsService;

    @Mock
    private ImageService imageService;

    @Mock
    private UserService userService;

    @Mock
    private AdsRepository adsRepository;

    @Mock
    private AdMapper adMapper;

    private MultipartFile mockImage;
    private CreateOrUpdateAd createOrUpdateAd;
    private UserEntity userEntity;
    private AdEntity adEntity;
    private Ad adDto;
    private ExtendedAd extendedAd;
    private Ads ads;

    @BeforeEach
    void setUp() {
        mockImage = Mockito.mock(MultipartFile.class);
        createOrUpdateAd = new CreateOrUpdateAd();
        createOrUpdateAd.setTitle("Test Ad");
        createOrUpdateAd.setDescription("Description");
        createOrUpdateAd.setPrice(100);

        userEntity = new UserEntity();
        userEntity.setId(1);

        adEntity = new AdEntity();
        adEntity.setId(1);
        adEntity.setTitle("Test Ad");
        adEntity.setDescription("Description");
        adEntity.setPrice(100);
        adEntity.setAuthor(userEntity);

        adDto = new Ad();
        adDto.setPk(1);
        adDto.setTitle("Test Ad");

        extendedAd = new ExtendedAd();
        extendedAd.setPk(1);
        extendedAd.setTitle("Test Ad");

        ads = new Ads();
        ads.setResults(List.of(adDto));
    }

    @Test
    void getAllAds_ReturnsAds() {
        when(adsRepository.findAll()).thenReturn(List.of(adEntity));
        when(adMapper.toAdsDto(anyList())).thenReturn(ads);

        assertEquals(ads, adsService.getAllAds());
        verify(adsRepository, times(1)).findAll();
        verify(adMapper, times(1)).toAdsDto(anyList());
    }

    @Test
    void addAd_CreatesNewAd() throws IOException {
        when(userService.getUserOrThrow("user")).thenReturn(userEntity);
        when(adMapper.toAdEntity(createOrUpdateAd)).thenReturn(adEntity);
        when(adsRepository.save(adEntity)).thenReturn(adEntity);
        when(adMapper.toAdDto(adEntity)).thenReturn(adDto);
        when(imageService.saveImage(adEntity, mockImage)).thenReturn(new byte[]{1, 2, 3});

        Ad result = adsService.addAd("user", createOrUpdateAd, mockImage);

        assertNotNull(result);
        assertEquals("Test Ad", result.getTitle());
        verify(userService, times(1)).getUserOrThrow("user");
        verify(adMapper, times(1)).toAdEntity(createOrUpdateAd);
        verify(imageService, times(1)).saveImage(adEntity, mockImage);
        verify(adsRepository, times(1)).save(adEntity);
        verify(adMapper, times(1)).toAdDto(adEntity);
    }

    @Test
    void getAds_ExistingAd_ReturnsExtendedAd() {
        when(adsRepository.findById(1)).thenReturn(Optional.of(adEntity));
        when(adMapper.toExtendedAdDto(adEntity)).thenReturn(extendedAd);

        ExtendedAd result = adsService.getAds(1);

        assertNotNull(result);
        assertEquals(1, result.getPk());
        verify(adsRepository, times(1)).findById(1);
        verify(adMapper, times(1)).toExtendedAdDto(adEntity);
    }

    @Test
    void getAds_AdNotFound_ThrowsNotFoundException() {
        when(adsRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> adsService.getAds(1));
        verify(adsRepository, times(1)).findById(1);
    }

    @Test
    void updateAds_ValidInput_ReturnsUpdatedAd() {
        when(adsRepository.findById(1)).thenReturn(Optional.of(adEntity));
        when(adsRepository.save(adEntity)).thenReturn(adEntity);
        when(adMapper.toAdDto(adEntity)).thenReturn(adDto);

        Ad result = adsService.updateAds("user", 1, createOrUpdateAd);

        assertNotNull(result);
        assertEquals("Test Ad", result.getTitle());
        verify(adsRepository, times(1)).findById(1);
        verify(adsRepository, times(1)).save(adEntity);
        verify(adMapper, times(1)).toAdDto(adEntity);
    }

    @Test
    void updateAds_AdNotFound_ThrowsNotFoundException() {
        when(adsRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> adsService.updateAds("user", 1, createOrUpdateAd));
    }

    @Test
    void removeAd_ValidInput_RemovesAd() {
        when(adsRepository.findById(1)).thenReturn(Optional.of(adEntity));

        adsService.removeAd("user", 1);

        verify(adsRepository, times(1)).findById(1);
        verify(adsRepository, times(1)).delete(adEntity);
    }

    @Test
    void removeAd_AdNotFound_ThrowsNotFoundException() {
        when(adsRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> adsService.removeAd("user", 1));
    }

    @Test
    void getAdsMe_ReturnsUserAds() {
        when(userService.getUserOrThrow("user")).thenReturn(userEntity);
        when(adsRepository.findAllByAuthor(userEntity)).thenReturn(List.of(adEntity));
        when(adMapper.toAdsDto(anyList())).thenReturn(ads);

        assertEquals(ads, adsService.getAdsMe("user"));
        verify(userService, times(1)).getUserOrThrow("user");
        verify(adsRepository, times(1)).findAllByAuthor(userEntity);
        verify(adMapper, times(1)).toAdsDto(anyList());
    }

    @Test
    void updateImage_ValidInput_ReturnsBytes() throws IOException {
        when(adsRepository.findById(1)).thenReturn(Optional.of(adEntity));
        when(imageService.saveImage(adEntity, mockImage)).thenReturn(new byte[]{1, 2, 3});

        byte[] result = adsService.updateImage("user", 1, mockImage);

        assertNotNull(result);
        assertArrayEquals(new byte[]{1, 2, 3}, result);
        verify(adsRepository, times(1)).findById(1);
        verify(imageService, times(1)).saveImage(adEntity, mockImage);
        verify(adsRepository, times(1)).save(adEntity);
    }

    @Test
    void updateImage_AdNotFound_ThrowsNotFoundException() {
        when(adsRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> adsService.updateImage("user", 1, mockImage));
    }
}
