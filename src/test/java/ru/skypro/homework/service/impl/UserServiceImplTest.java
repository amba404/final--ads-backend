package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.exception.NoRightsException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    private final String username = "test-user@mail.ru";
    private final String password = "password123";
    private final String usernameAdmin = "test-admin@mail.ru";
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userService;
    private UserEntity userEntityUser;
    private UserEntity userEntityAdmin;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(passwordEncoder.encode(anyString())).thenAnswer(i -> i.getArguments()[0]);
        when(passwordEncoder.matches(anyString(), anyString())).thenAnswer(i -> i.getArguments()[0].equals(i.getArguments()[1]));

        userEntityUser = new UserEntity();
        userEntityUser.setId(1);
        userEntityUser.setUsername(username);
        userEntityUser.setPassword(passwordEncoder.encode(password));
        userEntityUser.setFirstName("John");
        userEntityUser.setLastName("Doo");
        userEntityUser.setPhone("+71234567890");
        userEntityUser.setRole(Role.USER);

        userEntityAdmin = new UserEntity();
        userEntityAdmin.setId(1);
        userEntityAdmin.setUsername(usernameAdmin);
        userEntityAdmin.setPassword(passwordEncoder.encode(password));
        userEntityAdmin.setFirstName("Admin");
        userEntityAdmin.setLastName("Admin");
        userEntityAdmin.setPhone("+7 999 999 9999");
        userEntityAdmin.setRole(Role.ADMIN);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntityUser));
        when(userRepository.save(userEntityUser)).thenReturn(userEntityUser);

        when(userRepository.findByUsername(usernameAdmin)).thenReturn(Optional.of(userEntityAdmin));
        when(userRepository.save(userEntityAdmin)).thenReturn(userEntityAdmin);

    }

    @Test
    void setPassword_WithCorrectCurrentPassword_ShouldUpdatePassword() {
        NewPassword newPassword = new NewPassword();
        newPassword.setCurrentPassword(password);
        newPassword.setNewPassword("newPassword");


        userService.setPassword(username, newPassword);

        verify(userRepository).save(userEntityUser);
        assertEquals("newPassword", userEntityUser.getPassword());
    }

    @Test
    void setPassword_WithIncorrectCurrentPassword_ShouldThrowNoRightsException() {
        NewPassword newPassword = new NewPassword();
        newPassword.setCurrentPassword("wrongPassword");
        newPassword.setNewPassword("newWrongPassword");


        assertThrows(NoRightsException.class, () -> userService.setPassword(username, newPassword));
    }

    @Test
    void getUser_WhenUserExists_ShouldReturnUserDto() {
        User expectedUser = userMapper.toUserDto(userEntityUser);

        User result = userService.getUser(username);

        assertEquals(expectedUser, result);
    }

    @Test
    void updateUser_ShouldUpdateUserInfoAndReturnUpdatedDto() {
        UpdateUser updateUser = new UpdateUser();
        updateUser.setFirstName("John");
        updateUser.setLastName("Doe");
        updateUser.setPhone("+71234567899");

        when(userMapper.toUpdateUserDto(userEntityUser)).thenReturn(updateUser);
        UpdateUser result = userService.updateUser(username, updateUser);

        assertEquals(updateUser, result);
        verify(userRepository).save(userEntityUser);
    }

    @Test
    void userExists_ShouldReturnTrueWhenUserExists() {
        when(userRepository.existsByUsername(username)).thenReturn(true);

        assertTrue(userService.userExists(username));
    }

    @Test
    void userExists_ShouldReturnFalseWhenUserDoesNotExist() {
        when(userRepository.existsByUsername(username)).thenReturn(false);

        assertFalse(userService.userExists(username));
    }

    @Test
    void createUser_ShouldCreateUserWithEncodedPassword() {
        Register register = new Register();
        register.setUsername(username);
        register.setPassword(password);
        register.setFirstName("John");
        register.setLastName("Doe");
        register.setPhone("1234567890");
        register.setRole(Role.valueOf("USER"));

        when(userMapper.toUserEntity(register)).thenReturn(userEntityUser);

        userService.createUser(register);

        verify(userRepository).save(userEntityUser);
        assertEquals(password, userEntityUser.getPassword());
    }

    @Test
    void loadUserByUsername_ShouldReturnUserDetails() {
        UserDetails userDetails = userService.loadUserByUsername(username);

        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals(userEntityUser.getPassword(), userDetails.getPassword());
    }

    @Test
    void checkOwnerOrThrow_WhenUserIsNotAuthorAndIsAdmin_ShouldThrowNoRightsException() {
        UserEntity adminUser = new UserEntity();
        adminUser.setUsername("admin");
        adminUser.setRole(Role.ADMIN);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(adminUser));

        assertThrows(NoRightsException.class, () -> userService.checkOwnerOrThrow(username, userEntityUser));
    }

    @Test
    void checkOwnerOrThrow_WhenUserIsAuthor_ShouldNotThrowException() {
        UserEntity author = userEntityUser;
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntityUser));

        assertDoesNotThrow(() -> userService.checkOwnerOrThrow(username, author));
    }
}
