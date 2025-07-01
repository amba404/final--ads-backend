package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder encoder;
    private final ImageService imageService;

    @Override
    public void setPassword(String userName, NewPassword password) {
        UserEntity user = getUserOrThrow(userName);

        if (encoder.matches(password.getCurrentPassword(), user.getPassword())) {
            user.setPassword(encoder.encode(password.getNewPassword()));
            userRepository.save(user);
        } else {
            throw new RuntimeException("Current password is incorrect");
        }
    }

    @Override
    public User getUser(String userName) {
        UserEntity user = getUserOrThrow(userName);

        return userMapper.toUserDto(user);
    }

    @Override
    public UpdateUser updateUser(String userName, UpdateUser user) {
        UserEntity userEntity = getUserOrThrow(userName);

        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        userEntity.setPhone(user.getPhone());

        userRepository.save(userEntity);

        userEntity = getUserOrThrow(userName);

        return userMapper.toUpdateUserDto(userEntity);
    }

    @Override
    public byte[] updateUserImage(String userName, MultipartFile mFile) throws IOException {
        UserEntity userEntity = getUserOrThrow(userName);

        UUID imageId;
        if (userEntity.getImage() == null) {
            imageId = UUID.randomUUID();
        } else {
            imageId = userEntity.getImage().getId();
        }

        byte[] bytes = imageService.saveImage(imageId, mFile);

        userEntity.setImage(imageService.findById(imageId));
        userRepository.save(userEntity);

        return bytes;
    }

    @Override
    public boolean userExists(String userName) {
        return userRepository.existsByUsername(userName);
    }

    @Override
    public void createUser(Register register) {
        UserEntity userEntity = userMapper.toUserEntity(register);
        userEntity.setPassword(encoder.encode(register.getPassword()));
        userRepository.save(userEntity);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = getUserOrThrow(username);

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole().name()));

        return new org.springframework.security.core.userdetails
                .User(user.getUsername(), user.getPassword(), authorities);
    }

    @Override
    public UserEntity getUserOrThrow(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Override
    public void checkOwnerOrThrow(String username, UserEntity author) {
        UserEntity user = getUserOrThrow(username);

        if (!user.equals(author) && user.getRole().name().equals("ADMIN")) {
            throw new NoRightsException();
        }
    }
}
