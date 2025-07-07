package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.UserRepository;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AdsControllerTest {

    public static final int INVALID_AD_ID = -1;
    private static final String USER_NAME = "test@example.com";
    private static final String ADMIN_NAME = "admin@example.com";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AdsRepository adRepository;

    @Autowired
    private UserRepository userRepository;

    private AdEntity testAdUser, testAdAdmin;
    private UserEntity user, admin;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setPassword("password");
        user.setUsername(USER_NAME);
        user.setFirstName("Test");
        user.setLastName("User");
        user.setPhone("+7 (999) 999-99-99");
        user.setRole(Role.USER);
        user = userRepository.save(user);

        admin = new UserEntity();
        admin.setPassword("password");
        admin.setUsername(ADMIN_NAME);
        admin.setFirstName("Admin");
        admin.setLastName("Admin");
        admin.setPhone("+7 (000) 000-00-00");
        admin.setRole(Role.ADMIN);
        admin = userRepository.save(admin);

        testAdUser = new AdEntity();
        testAdUser.setTitle("Test Ad");
        testAdUser.setDescription("Test Description");
        testAdUser.setAuthor(user);
        testAdUser.setPrice(100);
        testAdUser = adRepository.save(testAdUser);

        testAdAdmin = new AdEntity();
        testAdAdmin.setTitle("Test Ad");
        testAdAdmin.setDescription("Test Description");
        testAdAdmin.setAuthor(admin);
        testAdAdmin.setPrice(100);
        testAdAdmin = adRepository.save(testAdAdmin);
    }

    @AfterEach
    void tearDown() {
        adRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void getAllAds_shouldReturn200() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/ads", testAdUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results").isArray());
    }

    @Test
    void addAd_All_Returned_Codes() throws Exception {
        CreateOrUpdateAd properties = new CreateOrUpdateAd();
        properties.setTitle("New Ad");
        properties.setDescription("New Description");
        properties.setPrice(2000);

        MockMultipartFile image = new MockMultipartFile("image", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "fake image".getBytes());
        MockMultipartFile jsonPart = new MockMultipartFile("properties", "", MediaType.APPLICATION_JSON_VALUE,
                new ObjectMapper().writeValueAsString(properties).getBytes());

        mockMvc.perform(multipart("/ads")
                        .file(jsonPart)
                        .file(image)
                        .with(anonymous()))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(multipart("/ads")
                        .file(jsonPart)
                        .file(image)
                        .with(user(USER_NAME)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Ad"));
    }

    @Test
    void getAd_All_Returned_Codes() throws Exception {

        mockMvc.perform(get("/ads/" + testAdUser.getId())
                        .with(user(USER_NAME)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk").value(testAdUser.getId()));

        mockMvc.perform(get("/ads/" + testAdUser.getId())
                        .with(anonymous()))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/ads/" + INVALID_AD_ID)
                        .with(user(USER_NAME)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteAd_All_Returned_Codes() throws Exception {

        //1. unauthorized
        mockMvc.perform(delete("/ads/" + testAdUser.getId())
                        .with(anonymous()))
                .andExpect(status().isUnauthorized());

        //2 User CAN NOT delete others Ads
        mockMvc.perform(delete("/ads/" + testAdAdmin.getId())
                        .with(user(USER_NAME)))
                .andExpect(status().isForbidden());

        //3 Admin CAN delete others Ads
        mockMvc.perform(delete("/ads/" + testAdUser.getId())
                        .with(user(ADMIN_NAME)))
                .andExpect(status().isNoContent());

        //4 Not found early deleted Ad
        mockMvc.perform(delete("/ads/" + testAdUser.getId())
                        .with(user(USER_NAME)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateAd_All_Returned_Codes() throws Exception {
        CreateOrUpdateAd properties = new CreateOrUpdateAd();
        properties.setTitle("New Ad");
        properties.setDescription("New Description");
        properties.setPrice(2000);

        String jsonBody = (new ObjectMapper()).writeValueAsString(properties);

        //1. unauthorized
        mockMvc.perform(patch("/ads/" + testAdUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody)
                        .with(anonymous()))
                .andExpect(status().isUnauthorized());

        //2 User CAN NOT patch others Ads
        mockMvc.perform(patch("/ads/" + testAdAdmin.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody)
                        .with(user(USER_NAME)))
                .andExpect(status().isForbidden());

        //3 Admin CAN patch others Ads
        mockMvc.perform(patch("/ads/" + testAdUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody)
                        .with(user(ADMIN_NAME)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Ad"));

        //4 Not found Ad
        mockMvc.perform(patch("/ads/" + INVALID_AD_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody)
                        .with(user(USER_NAME)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAdsMe_All_Returned_Codes() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/ads/me")
                        .with(user(USER_NAME)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results").isArray());

        mockMvc.perform(get("/ads/me")
                        .with(anonymous()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateImage_All_Returned_Codes() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "new image".getBytes());

        String uriTemplateUser = "/ads/" + testAdUser.getId() + "/image";
        String uriTemplateAdmin = "/ads/" + testAdAdmin.getId() + "/image";
        String uriTemplateInvalidAd = "/ads/" + INVALID_AD_ID + "/image";

        //1 Anonymous unauthorized
        mockMvc.perform(multipart(HttpMethod.PATCH, uriTemplateUser)
                        .file(image)
                        .with(anonymous()))
                .andExpect(status().isUnauthorized());

        //2 Not found ad
        mockMvc.perform(multipart(HttpMethod.PATCH, uriTemplateInvalidAd)
                        .file(image)
                        .with(user(USER_NAME)))
                .andExpect(status().isNotFound());

        //3 User CAN NOT patch others Ads
        mockMvc.perform(multipart(HttpMethod.PATCH, uriTemplateAdmin)
                        .file(image)
                        .with(user(USER_NAME)))
                .andExpect(status().isForbidden());

        //4 Admin CAN patch others Ads
        mockMvc.perform(multipart(HttpMethod.PATCH, uriTemplateUser)
                        .file(image)
                        .with(user(ADMIN_NAME)))
                .andExpect(status().isOk());
    }
}
