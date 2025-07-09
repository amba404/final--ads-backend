package ru.skypro.homework.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.model.ImageEntity;
import ru.skypro.homework.model.UserEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapperImpl();
    }

    @Test
    void toUserEntity_ValidRegister_ShouldMapCorrectly() {
        Register register = new Register();
        register.setUsername("a@a.ru");
        register.setPassword("password123");
        register.setFirstName("John");
        register.setLastName("Doe");
        register.setPhone("+71234567890");
        register.setRole(Role.valueOf("USER"));

        UserEntity result = userMapper.toUserEntity(register);

        assertNotNull(result);
        assertNull(result.getId());
        assertNull(result.getImage());
        assertEquals(register.getUsername(), result.getUsername());
        assertEquals(register.getPassword(), result.getPassword());
        assertEquals(register.getFirstName(), result.getFirstName());
        assertEquals(register.getLastName(), result.getLastName());
        assertEquals(register.getPhone(), result.getPhone());
        assertEquals(register.getRole(), result.getRole());
    }

    @Test
    void toUserDto_UserWithNullImage_ShouldReturnNullForImage() {
        UserEntity user = new UserEntity();
        user.setId(1);
        user.setUsername("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPhone("+71234567890");

        User result = userMapper.toUserDto(user);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(user.getUsername(), result.getEmail());
        assertEquals(user.getFirstName(), result.getFirstName());
        assertEquals(user.getLastName(), result.getLastName());
        assertEquals(user.getPhone(), result.getPhone());
        assertNull(result.getImage());
    }

    @Test
    void toUserDto_UserWithImage_ShouldMapImageUrlCorrectly() {
        ImageEntity image = new ImageEntity();
        image.setId(UUID.randomUUID());

        UserEntity user = new UserEntity();
        user.setId(1);
        user.setUsername("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPhone("+71234567890");
        user.setImage(image);

        User result = userMapper.toUserDto(user);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(user.getUsername(), result.getEmail());
        assertEquals(user.getFirstName(), result.getFirstName());
        assertEquals(user.getLastName(), result.getLastName());
        assertEquals(user.getPhone(), result.getPhone());
        assertEquals(image.getUrl(), result.getImage());
    }

    @Test
    void toUpdateUserDto_ValidUserEntity_ShouldMapCorrectly() {
        UserEntity user = new UserEntity();
        user.setId(1);
        user.setUsername("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPhone("+71234567890");

        UpdateUser result = userMapper.toUpdateUserDto(user);

        assertNotNull(result);
        assertEquals(user.getFirstName(), result.getFirstName());
        assertEquals(user.getLastName(), result.getLastName());
        assertEquals(user.getPhone(), result.getPhone());
    }
}