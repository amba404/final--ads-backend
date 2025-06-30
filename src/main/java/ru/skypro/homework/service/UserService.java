package ru.skypro.homework.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;

import java.io.IOException;

public interface UserService extends UserDetailsService {
    void setPassword(String userName, NewPassword password);

    User getUser(String userName);

    UpdateUser updateUser(String userName, UpdateUser user);

    byte[] updateUserImage(String userName, MultipartFile mFile) throws IOException;

    boolean userExists(String userName);

    void createUser(Register user);
}
