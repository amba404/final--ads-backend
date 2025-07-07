package ru.skypro.homework.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.ImageEntity;
import ru.skypro.homework.model.UserEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AdMapperTest {

    //    @Autowired
    private AdMapper adMapper;

    @BeforeEach
    void setUp() {
        adMapper = new AdMapperImpl(); // Замените на реальную реализацию MapStruct, если есть
    }

    @Test
    void toAdDto_WithValidData_ShouldMapCorrectly() {
        UserEntity author = new UserEntity();
        author.setId(1);

        ImageEntity image = new ImageEntity();
        image.setId(UUID.randomUUID());

        AdEntity adEntity = new AdEntity();
        adEntity.setId(100);
        adEntity.setTitle("Test Ad");
        adEntity.setPrice(5000);
        adEntity.setAuthor(author);
        adEntity.setImage(image);

        Ad result = adMapper.toAdDto(adEntity);

        assertNotNull(result);
        assertEquals(100, result.getPk());
        assertEquals(1, result.getAuthor());
        assertEquals(image.getUrl(), result.getImage());
        assertEquals("Test Ad", result.getTitle());
        assertEquals(5000, result.getPrice());
    }

    @Test
    void toAdDto_WithNullImage_ShouldReturnNullForImage() {
        AdEntity adEntity = new AdEntity();
        adEntity.setId(100);
        adEntity.setTitle("No Image Ad");
        adEntity.setPrice(3000);

        Ad result = adMapper.toAdDto(adEntity);

        assertNotNull(result);
        assertNull(result.getImage());
    }

    @Test
    void toExtendedAdDto_WithValidData_ShouldMapCorrectly() {
        UserEntity author = new UserEntity();
        author.setId(1);
        author.setFirstName("John");
        author.setLastName("Doe");
        author.setUsername("john.doe@example.com");
        author.setPhone("+71234567890");

        ImageEntity image = new ImageEntity();
        image.setId(UUID.randomUUID());

        AdEntity adEntity = new AdEntity();
        adEntity.setId(100);
        adEntity.setTitle("Extended Ad");
        adEntity.setPrice(7000);
        adEntity.setAuthor(author);
        adEntity.setImage(image);

        ExtendedAd result = adMapper.toExtendedAdDto(adEntity);

        assertNotNull(result);
        assertEquals(100, result.getPk());
        assertEquals("John", result.getAuthorFirstName());
        assertEquals("Doe", result.getAuthorLastName());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals("+71234567890", result.getPhone());
        assertEquals(image.getUrl(), result.getImage());
        assertEquals("Extended Ad", result.getTitle());
        assertEquals(7000, result.getPrice());
    }

    @Test
    void toAdEntity_ShouldIgnoreFieldsAndMapOnlyTitleAndPrice() {
        CreateOrUpdateAd createOrUpdateAd = new CreateOrUpdateAd();
        createOrUpdateAd.setTitle("New Ad");
        createOrUpdateAd.setPrice(10000);

        AdEntity result = adMapper.toAdEntity(createOrUpdateAd);

        assertNotNull(result);
        assertEquals("New Ad", result.getTitle());
        assertEquals(10000, result.getPrice());
        assertEquals(0, result.getId()); // должно быть проигнорировано
        assertNull(result.getAuthor()); // должно быть проигнорировано
        assertNull(result.getImage()); // должно быть проигнорировано
        assertNull(result.getComments()); // должно быть проигнорировано
    }

    @Test
    void toAdsDto_WithEmptyList_ShouldReturnEmptyAds() {
        Ads result = adMapper.toAdsDto(List.of());

        assertNotNull(result);
        assertEquals(0, result.getCount());
        assertNotNull(result.getResults());
        assertEquals(0, result.getResults().size());
    }

    @Test
    void toAdsDto_WithNonNullList_ShouldReturnMappedAds() {
        AdEntity ad1 = new AdEntity();
        ad1.setId(1);
        ad1.setTitle("Ad 1");
        ad1.setPrice(1000);

        AdEntity ad2 = new AdEntity();
        ad2.setId(2);
        ad2.setTitle("Ad 2");
        ad2.setPrice(2000);

        List<AdEntity> ads = List.of(ad1, ad2);
        Ads result = adMapper.toAdsDto(ads);

        assertNotNull(result);
        assertEquals(2, result.getCount());
        assertEquals(2, result.getResults().size());

        Ad dto1 = result.getResults().get(0);
        assertEquals(1, dto1.getPk());
        assertEquals("Ad 1", dto1.getTitle());
        assertEquals(1000, dto1.getPrice());

        Ad dto2 = result.getResults().get(1);
        assertEquals(2, dto2.getPk());
        assertEquals("Ad 2", dto2.getTitle());
        assertEquals(2000, dto2.getPrice());
    }
}
