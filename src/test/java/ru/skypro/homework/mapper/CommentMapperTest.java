package ru.skypro.homework.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.model.CommentEntity;
import ru.skypro.homework.model.ImageEntity;
import ru.skypro.homework.model.UserEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CommentMapperTest {

    private CommentMapper commentMapper;

    @BeforeEach
    void setUp() {
        // Получаем реализацию через MapStruct (реализация генерируется автоматически)
        commentMapper = new CommentMapperImpl(); // Убедитесь, что есть CommentMapperImpl.java
    }

    @Test
    void toComment_WithNullAuthor_ShouldThrowIllegalArgumentException() {
        CommentEntity comment = new CommentEntity();
        comment.setId(1);
        comment.setText("Test comment");
        comment.setCreatedAt(System.currentTimeMillis());
        comment.setAuthor(null);

        assertThrows(IllegalArgumentException.class, () -> commentMapper.toComment(comment));
    }

    @Test
    void toComment_WithAuthorWithoutImage_ShouldSetAuthorImageToNull() {
        UserEntity user = new UserEntity();
        user.setId(100);
        user.setFirstName("John");

        CommentEntity comment = new CommentEntity();
        comment.setId(1);
        comment.setText("Test comment");
        comment.setCreatedAt(System.currentTimeMillis());
        comment.setAuthor(user);

        Comment result = commentMapper.toComment(comment);

        assertNotNull(result);
        assertEquals(1, result.getPk());
        assertEquals("Test comment", result.getText());
        assertEquals(100, result.getAuthor());
        assertEquals("John", result.getAuthorFirstName());
        assertNull(result.getAuthorImage());
    }

    @Test
    void toComment_WithFullData_ShouldMapAllFieldsCorrectly() {
        ImageEntity image = new ImageEntity();
        image.setId(UUID.randomUUID());

        UserEntity user = new UserEntity();
        user.setId(100);
        user.setFirstName("John");
        user.setImage(image);

        CommentEntity comment = new CommentEntity();
        comment.setId(1);
        comment.setText("Test comment");
        comment.setCreatedAt(System.currentTimeMillis());
        comment.setAuthor(user);

        Comment result = commentMapper.toComment(comment);

        assertNotNull(result);
        assertEquals(1, result.getPk());
        assertEquals("Test comment", result.getText());
        assertEquals(100, result.getAuthor());
        assertEquals("John", result.getAuthorFirstName());
        assertEquals(image.getUrl(), result.getAuthorImage());
    }

    @Test
    void toComments_WithEmptyList_ShouldReturnEmptyComments() {
        Comments result = commentMapper.toComments(List.of());

        assertNotNull(result);
        assertEquals(0, result.getCount());
        assertNotNull(result.getResults());
        assertTrue(result.getResults().isEmpty());
    }

    @Test
    void toComments_WithNonNullList_ShouldMapAllCommentsCorrectly() {
        ImageEntity image = new ImageEntity();
        image.setId(UUID.randomUUID());

        UserEntity user = new UserEntity();
        user.setId(100);
        user.setFirstName("John");
        user.setImage(image);

        CommentEntity comment1 = new CommentEntity();
        comment1.setId(1);
        comment1.setText("Comment 1");
        comment1.setCreatedAt(System.currentTimeMillis());
        comment1.setAuthor(user);

        CommentEntity comment2 = new CommentEntity();
        comment2.setId(2);
        comment2.setText("Comment 2");
        comment2.setCreatedAt(System.currentTimeMillis());
        comment2.setAuthor(user);

        List<CommentEntity> comments = List.of(comment1, comment2);
        Comments result = commentMapper.toComments(comments);

        assertNotNull(result);
        assertEquals(2, result.getCount());
        assertEquals(2, result.getResults().size());

        Comment dto1 = result.getResults().get(0);
        assertEquals(1, dto1.getPk());
        assertEquals("Comment 1", dto1.getText());
        assertEquals(100, dto1.getAuthor());
        assertEquals("John", dto1.getAuthorFirstName());
        assertEquals(image.getUrl(), dto1.getAuthorImage());

        Comment dto2 = result.getResults().get(1);
        assertEquals(2, dto2.getPk());
        assertEquals("Comment 2", dto2.getText());
        assertEquals(100, dto2.getAuthor());
        assertEquals("John", dto2.getAuthorFirstName());
        assertEquals(image.getUrl(), dto2.getAuthorImage());
    }

    @Test
    void toComments_WithNullList_ShouldReturnEmptyComments() {
        Comments result = commentMapper.toComments(null);

        assertNotNull(result);
        assertEquals(0, result.getCount());
        assertNotNull(result.getResults());
        assertTrue(result.getResults().isEmpty());
    }
}

