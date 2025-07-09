package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.CommentEntity;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.UserService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private AdsRepository adsRepository;

    @Mock
    private UserService userService;

    @Mock
    private AdsService adsService;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private CommentServiceImpl commentService;

    private UserEntity user;
    private AdEntity ad;
    private CommentEntity commentEntity;
    private CreateOrUpdateComment createOrUpdateComment;
    private Comment comment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new UserEntity();
        user.setId(1);
        user.setUsername("testUser");

        ad = new AdEntity();
        ad.setId(100);

        commentEntity = new CommentEntity();
        commentEntity.setId(1);
        commentEntity.setAuthor(user);
        commentEntity.setAd(ad);
        commentEntity.setText("Test comment");
        commentEntity.setCreatedAt(System.currentTimeMillis());

        createOrUpdateComment = new CreateOrUpdateComment();
        createOrUpdateComment.setText("Updated comment");

        comment = new Comment();
        comment.setPk(1);
        comment.setAuthor(user.getId());
        comment.setText("Test comment");
        comment.setCreatedAt(System.currentTimeMillis());
    }

    @Test
    void getComments_success() {
        Comments comments = new Comments();
        comments.setResults(List.of(comment));
        comments.setCount(1);

        when(commentRepository.findAllByAdId(100)).thenReturn(List.of(commentEntity));
        when(adsRepository.existsById(100)).thenReturn(true);
        when(commentMapper.toComments(List.of(commentEntity))).thenReturn(comments);

        Comments result = commentService.getComments(100);

        assertNotNull(result);
        assertEquals(1, result.getResults().size());
        verify(commentRepository, times(1)).findAllByAdId(100);
    }

    @Test
    void addComment_success() throws NotFoundException {
        when(userService.getUserOrThrow("testUser")).thenReturn(user);
        when(adsService.getAdOrThrow(100)).thenReturn(ad);
        when(commentRepository.save(any(CommentEntity.class))).thenReturn(commentEntity);
        when(commentMapper.toComment(commentEntity)).thenReturn(comment);

        Comment result = commentService.addComment("testUser", 100, createOrUpdateComment);

        assertNotNull(result);
        assertEquals("Test comment", result.getText());
        verify(userService, times(1)).getUserOrThrow("testUser");
        verify(adsService, times(1)).getAdOrThrow(100);
        verify(commentRepository, times(1)).save(any(CommentEntity.class));
    }

    @Test
    void deleteComment_success() throws NotFoundException {
        when(commentRepository.findById(1)).thenReturn(Optional.of(commentEntity));
        when(commentRepository.existsByAdId(100)).thenReturn(true);

        commentService.deleteComment("testUser", 100, 1);

        verify(userService, times(1)).checkOwnerOrThrow("testUser", user);
        verify(commentRepository, times(1)).delete(commentEntity);
    }

    @Test
    void updateComment_success() throws NotFoundException {
        when(commentRepository.findById(1)).thenReturn(Optional.of(commentEntity));
        when(commentRepository.existsByAdId(100)).thenReturn(true);
        when(commentRepository.save(any(CommentEntity.class))).thenReturn(commentEntity);
        when(commentMapper.toComment(commentEntity)).thenReturn(comment);

        Comment result = commentService.updateComment("testUser", 100, 1, createOrUpdateComment);

        assertNotNull(result);
        assertEquals("Updated comment", commentEntity.getText());
        verify(userService, times(1)).checkOwnerOrThrow("testUser", user);
        verify(commentRepository, times(1)).save(commentEntity);
    }

    @Test
    void getCommentOrThrow_commentNotFound() {
        when(commentRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> commentService.getCommentOrThrow(1));
        verify(commentRepository, times(1)).findById(1);
    }
}
