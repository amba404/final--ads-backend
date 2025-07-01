package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.UserService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final PasswordEncoder encoder;


    @Override
    public boolean login(String userName, String password) {
        if (!userService.userExists(userName)) {
            return false;
        }
        UserDetails userDetails = userService.loadUserByUsername(userName);
        return encoder.matches(password, userDetails.getPassword());
    }

    @Override
    public boolean register(Register register) {
        if (userService.userExists(register.getUsername())) {
            return false;
        }
        userService.createUser(register);
        return true;
    }

}
