package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;

public interface UserService {
    boolean setPassword(NewPassword password);

    User getCurrentUser();

    boolean updateUser(UpdateUser user);

    boolean updateUserImage(MultipartFile file);
}
