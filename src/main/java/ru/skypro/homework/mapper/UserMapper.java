package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.model.UserEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "ads", ignore = true)
    UserEntity toUserEntity(Register register);

    @Mapping(target = "email", source = "username")
    @Mapping(target = "image", expression = "java(user.getImage() != null ? user.getImage().getUrl() : null)")
    User toUserDto(UserEntity user);

    UpdateUser toUpdateUserDto(UserEntity userEntity);
}
