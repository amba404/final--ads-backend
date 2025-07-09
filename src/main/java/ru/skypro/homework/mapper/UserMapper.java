package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.model.UserEntity;

/**
 * Маппинг сущности пользователя
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    /**
     * Маппинг структуры для регистрации пользователя в сущность пользователя
     * @param register структура с данными для регистрации {@link Register}
     * @return сущность пользователя {@link UserEntity}
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "ads", ignore = true)
    @Mapping(target = "comments", ignore = true)
    UserEntity toUserEntity(Register register);

    /**
     * Маппинг сущности пользователя в структуру для отображения
     * @param user сущность пользователя {@link UserEntity}
     * @return DTO {@link User}
     */
    @Mapping(target = "email", source = "username")
    @Mapping(target = "image", expression = "java(user.getImage() != null ? user.getImage().getUrl() : null)")
    User toUserDto(UserEntity user);

    /**
     * Маппинг сущности пользователя в структуру для обновления пользователя
     * @param userEntity сущность пользователя {@link UserEntity}
     * @return DTO {@link UpdateUser}
     */
    UpdateUser toUpdateUserDto(UserEntity userEntity);
}
