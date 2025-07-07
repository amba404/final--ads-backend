package ru.skypro.homework.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.CommentEntity;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CommentControllerTest {

    public static final int INVALID_AD_ID = -1;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AdsRepository adRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    private static final String USER_NAME = "test@example.com";
    private static final String ADMIN_NAME = "admin@example.com";
    private AdEntity testAd;
    private UserEntity user, admin;
    private CommentEntity testCommentUser, testCommentAdmin;

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

        testAd = new AdEntity();
        testAd.setTitle("Test Ad");
        testAd.setDescription("Test Description");
        testAd.setAuthor(user);
        testAd.setPrice(100);
        testAd = adRepository.save(testAd);

        testCommentUser = new CommentEntity();
        testCommentUser.setText("Initial comment");
        testCommentUser.setAd(testAd);
        testCommentUser.setAuthor(user);
        testCommentUser.setCreatedAt(System.currentTimeMillis());
        testCommentUser = commentRepository.save(testCommentUser);

        testCommentAdmin = new CommentEntity();
        testCommentAdmin.setText("Initial comment");
        testCommentAdmin.setAd(testAd);
        testCommentAdmin.setAuthor(admin);
        testCommentAdmin.setCreatedAt(System.currentTimeMillis());
        testCommentAdmin = commentRepository.save(testCommentAdmin);
    }

    @AfterEach
    void tearDown() {
        commentRepository.deleteAll();
        adRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = USER_NAME, roles = "USER")
    void getComments_shouldReturnComments() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/ads/{id}/comments", testAd.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results").isArray());
    }

    @Test
    void getComments_shouldReturn401() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/ads/{id}/comments", testAd.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = USER_NAME, roles = "USER")
    void getComments_shouldReturn404() throws Exception {

        // Act & Assert
        mockMvc.perform(get("/ads/{id}/comments", INVALID_AD_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = USER_NAME, roles = "USER")
    void addComment_shouldReturnCreatedComment() throws Exception {
        CreateOrUpdateComment createComment = new CreateOrUpdateComment();
        createComment.setText("New comment");

        // Act & Assert
        mockMvc.perform(post("/ads/{id}/comments", testAd.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\":\"New comment\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("New comment"));
    }

    @Test
    void addComment_shouldReturn401() throws Exception {
        CreateOrUpdateComment createComment = new CreateOrUpdateComment();
        createComment.setText("New comment");

        // Act & Assert
        mockMvc.perform(post("/ads/{id}/comments", testAd.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\":\"New comment\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = USER_NAME, roles = "USER")
    void addComment_shouldReturn404() throws Exception {
        CreateOrUpdateComment createComment = new CreateOrUpdateComment();
        createComment.setText("New comment");

        // Act & Assert
        mockMvc.perform(post("/ads/{id}/comments", INVALID_AD_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\":\"New comment\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = USER_NAME, roles = "USER")
    void deleteComment_shouldReturn200() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/ads/{id}/comments/{commentId}", testAd.getId(), testCommentUser.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = USER_NAME, roles = "USER")
    void deleteComment_shouldReturn403() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/ads/{id}/comments/{commentId}", testAd.getId(), testCommentAdmin.getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = ADMIN_NAME, roles = "ADMIN")
    void deleteCommentAdmin_shouldReturn200() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/ads/{id}/comments/{commentId}", testAd.getId(), testCommentUser.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = USER_NAME, roles = "USER")
    void updateComment_shouldReturnUpdatedComment() throws Exception {
        CreateOrUpdateComment updatedComment = new CreateOrUpdateComment();
        updatedComment.setText("Updated text");

        // Act & Assert
        mockMvc.perform(patch("/ads/{id}/comments/{commentId}", testAd.getId(), testCommentUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\":\"Updated text\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Updated text"));
    }

    @Test
    //@WithMockUser(username = USER_NAME, roles = "USER")
    void updateComment_shouldReturn401() throws Exception {
        CreateOrUpdateComment updatedComment = new CreateOrUpdateComment();
        updatedComment.setText("Updated text");

        // Act & Assert
        mockMvc.perform(patch("/ads/{id}/comments/{commentId}", testAd.getId(), testCommentUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\":\"Updated text\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = USER_NAME, roles = "USER")
    void updateComment_shouldReturn403() throws Exception {
        CreateOrUpdateComment updatedComment = new CreateOrUpdateComment();
        updatedComment.setText("Updated text");

        // Act & Assert
        mockMvc.perform(patch("/ads/{id}/comments/{commentId}", testAd.getId(), testCommentAdmin.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\":\"Updated text\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = ADMIN_NAME)
    void updateCommentAdmin_shouldReturn200() throws Exception {
        CreateOrUpdateComment updatedComment = new CreateOrUpdateComment();
        updatedComment.setText("Updated text");

        // Act & Assert
        mockMvc.perform(patch("/ads/{id}/comments/{commentId}", testAd.getId(), testCommentUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\":\"Updated text\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Updated text"));
    }

    @Test
    @WithMockUser(username = USER_NAME, roles = "USER")
    void updateComment_shouldReturn404() throws Exception {
        CreateOrUpdateComment updatedComment = new CreateOrUpdateComment();
        updatedComment.setText("Updated text");

        // Act & Assert
        mockMvc.perform(patch("/ads/{id}/comments/{commentId}", INVALID_AD_ID, testCommentAdmin.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\":\"Updated text\"}"))
                .andExpect(status().isNotFound());
    }

}
