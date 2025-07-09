package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.exception.NoRightsException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserService;

import java.io.IOException;
import java.util.List;

/**
 * Реализация сервиса для работы с пользователями
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder encoder;
    private final ImageService imageService;
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    /**
     * Метод для смены пароля
     * @param userName имя пользователя
     * @param password новый пароль в виде структуры {@link NewPassword}
     * @throws NoRightsException если пароль, указанный как текущий, не совпадает с текущим в БД
     */
    @Override
    public void setPassword(String userName, NewPassword password) {
        UserEntity user = getUserOrThrow(userName);

        if (encoder.matches(password.getCurrentPassword(), user.getPassword())) {
            user.setPassword(encoder.encode(password.getNewPassword()));
            userRepository.save(user);
        } else {
            throw new NoRightsException("Current password is incorrect");
        }
    }

    /**
     * Метод для получения информации о пользователе
     * @param userName имя пользователя
     * @return DTO {@link User}
     * @throws UsernameNotFoundException если пользователя с таким именем не существует в БД
     */
    @Override
    public User getUser(String userName) {
        UserEntity user = getUserOrThrow(userName);

        return userMapper.toUserDto(user);
    }

    /**
     * Метод для обновления информации о пользователе
     * @param userName имя пользователя
     * @param user новая информация о пользователе в виде структуры {@link UpdateUser}
     * @return DTO {@link UpdateUser}
     * @throws UsernameNotFoundException если пользователя с таким именем не существует в БД
     */
    @Override
    public UpdateUser updateUser(String userName, UpdateUser user) {
        UserEntity userEntity = getUserOrThrow(userName);

        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        userEntity.setPhone(user.getPhone());

        return userMapper.toUpdateUserDto(userRepository.save(userEntity));
    }

    /**
     * Метод для обновления аватара пользователя
     * @param userName имя пользователя
     * @param mFile файл с новым аватаром {@link MultipartFile}
     * @return массив байт файла с новым аватаром
     * @throws UsernameNotFoundException если пользователя с таким именем не существует в БД
     */
    @Override
    public byte[] updateUserImage(String userName, MultipartFile mFile) throws IOException {
        UserEntity userEntity = getUserOrThrow(userName);

        byte[] bytes = imageService.saveImage(userEntity, mFile);

        userRepository.save(userEntity);

        return bytes;
    }

    /**
     * Метод для проверки существования пользователя по имени
     * @param userName имя пользователя
     * @return true если пользователь с таким именем существует в БД, иначе false
     */
    @Override
    public boolean userExists(String userName) {
        return userRepository.existsByUsername(userName);
    }

    /**
     * Метод для создания нового пользователя.
     * @param register новый пользователь в виде структуры {@link Register}
     * @return true если пользователь ранее не был создан и успешно создан, иначе false
     */
    @Override
    public boolean createUser(Register register) {
        if (userExists(register.getUsername())) {
            return false;
        }
        UserEntity userEntity = userMapper.toUserEntity(register);

        userEntity.setPassword(encoder.encode(register.getPassword()));

        userRepository.save(userEntity);

        return userExists(register.getUsername());
    }

    /**
     * Метод для получения информации о пользователе по имени, согласно контракту {@link UserDetailsService}
     * @param username имя пользователя
     * @return {@link UserDetails}
     * @throws UsernameNotFoundException если пользователя с таким именем не существует в БД
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("loadUserByUsername: {}", username);

        UserEntity user = getUserOrThrow(username);

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole().name()));

        return new org.springframework.security.core.userdetails
                .User(user.getUsername(), user.getPassword(), authorities);
    }

    /**
     * Метод для получения пользователе по имени
     * @param username имя пользователя
     * @return {@link UserEntity}
     * @throws UsernameNotFoundException если пользователя с таким именем не существует в БД
     */
    @Override
    public UserEntity getUserOrThrow(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    /**
     * Метод для проверки прав пользователя.
     * Проверяемый пользователь должен быть автором (указан в параметре) либо иметь права администратора.
     * @param username имя пользователя
     * @param author автор объекта, по отношению к которому проверяются права текущего пользователя, {@link UserEntity}
     * @throws NoRightsException если пользователь не является автором объекта и не имеет прав администратора
     */
    @Override
    public void checkOwnerOrThrow(String username, UserEntity author) {
        UserEntity user = getUserOrThrow(username);

        if (!(user.equals(author) || user.getRole().name().equals("ADMIN"))) {
            throw new NoRightsException();
        }
    }
}
