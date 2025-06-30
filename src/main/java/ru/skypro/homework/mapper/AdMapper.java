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

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AdMapper {

    @Mapping(target = "pk", source = "id")
    @Mapping(target = "author", source = "author.id")
    @Mapping(target = "image", expression = "java(ad.getImage() != null ? ad.getImage().getUrl() : null)")
    Ad toAdDto(AdEntity ad);

    @Mapping(target = "pk", source = "id")
    @Mapping(target = "authorFirstName", source = "author.firstName")
    @Mapping(target = "authorLastName", source = "author.lastName")
    @Mapping(target = "email", source = "author.username")
    @Mapping(target = "phone", source = "author.phone")
    @Mapping(target = "image", expression = "java(ad.getImage() != null ? ad.getImage().getUrl() : null)")
    ExtendedAd toExtendedAdDto(AdEntity ad);

    AdEntity toAdEntity(CreateOrUpdateAd ad);

    default Ads toAdsDto(List<AdEntity> ads) {
        Ads adsDto = new Ads();

        if(ads == null || ads.isEmpty()) {
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
