package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public boolean setPassword(NewPassword password) {
        return false;
    }

    @Override
    public User getUser() {
        return new User();
    }

    @Override
    public UpdateUser updateUser(UpdateUser user) {
        return new UpdateUser();
    }

    @Override
    public byte[] updateUserImage(MultipartFile image) {
        return new byte[0];
    }
}
