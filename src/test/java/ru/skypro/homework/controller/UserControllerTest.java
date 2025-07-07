package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {
    private static final String USER_NAME = "test@example.com";
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;
    private UserEntity user;
    private NewPassword newPassword;
    private UpdateUser updateUser;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setPassword(encoder.encode("password"));
        user.setUsername(USER_NAME);
        user.setFirstName("Test");
        user.setLastName("User");
        user.setPhone("+7 (999) 999-99-99");
        user.setRole(Role.USER);
        user = userRepository.save(user);

        newPassword = new NewPassword();
        newPassword.setCurrentPassword("password");
        newPassword.setNewPassword("newPassword");

        updateUser = new UpdateUser();
        updateUser.setFirstName("NewName");
        updateUser.setLastName("NewName");
        updateUser.setPhone("+7 (888) 999-99-99");
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = USER_NAME)
    void setPassword_ShouldReturn200() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(newPassword);

        mockMvc.perform(post("/users/set_password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk());
    }

    @Test
    void setPassword_ShouldReturn401() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(newPassword);

        mockMvc.perform(post("/users/set_password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = USER_NAME)
    void get_ShouldReturn200() throws Exception {
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(USER_NAME));
    }

    @Test
    void get_ShouldReturn401() throws Exception {
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = USER_NAME)
    void update_ShouldReturn200() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(updateUser);

        mockMvc.perform(patch("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(updateUser.getFirstName()));
    }

    @Test
    void update_ShouldReturn401() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(updateUser);

        mockMvc.perform(patch("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isUnauthorized());
    }
}
