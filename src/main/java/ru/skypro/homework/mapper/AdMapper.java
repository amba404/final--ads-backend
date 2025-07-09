package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.model.AdEntity;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Интерфейс маппера для сущности AdEntity
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AdMapper {

    /**
     * Метод маппинга сущности AdEntity в DTO Ad
     * @param ad сущность {@link AdEntity}
     * @return DTO {@link Ad}
     */
    @Mapping(target = "pk", source = "id")
    @Mapping(target = "author", source = "author.id")
    @Mapping(target = "image", expression = "java(ad.getImage() != null ? ad.getImage().getUrl() : null)")
    Ad toAdDto(AdEntity ad);

    /**
     * Метод маппинга сущности AdEntity в DTO ExtendedAd
     * @param ad сущность {@link AdEntity}
     * @return DTO {@link ExtendedAd}
     */
    @Mapping(target = "pk", source = "id")
    @Mapping(target = "authorFirstName", source = "author.firstName")
    @Mapping(target = "authorLastName", source = "author.lastName")
    @Mapping(target = "email", source = "author.username")
    @Mapping(target = "phone", source = "author.phone")
    @Mapping(target = "image", expression = "java(ad.getImage() != null ? ad.getImage().getUrl() : null)")
    ExtendedAd toExtendedAdDto(AdEntity ad);

    /**
     * Метод маппинга DTO CreateOrUpdateAd в сущность AdEntity
     * @param ad DTO {@link CreateOrUpdateAd}
     * @return сущность {@link AdEntity}
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "comments", ignore = true)
    AdEntity toAdEntity(CreateOrUpdateAd ad);

    /**
     * Метод маппинга списка сущностей AdEntity в DTO Ads
     * @param ads список сущностей {@link List} <{@link AdEntity}>
     * @return DTO {@link Ads}
     */
    default Ads toAdsDto(List<AdEntity> ads) {
        Ads adsDto = new Ads();

        if (ads == null || ads.isEmpty()) {
            adsDto.setResults(List.of());
            adsDto.setCount(0);
        } else {
            adsDto.setCount(ads.size());
            List<Ad> adsDtoList = ads.stream().map(this::toAdDto).collect(Collectors.toList());
            adsDto.setResults(adsDtoList);
        }

        return adsDto;
    }
}
