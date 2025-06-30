package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.exception.NoRightsException;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.CommentEntity;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.service.UserService;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final AdsService adsService;
    private final CommentMapper commentMapper;

    @Override
    public Comments getComments(int id) {
        return commentMapper.toComments(commentRepository.findAllByAdId(id));
    }

    @Override
    public Comment addComment(String username, int id, CreateOrUpdateComment comment) {
        UserEntity user = userService.getUserOrThrow(username);
        AdEntity ad = adsService.getAdOrThrow(id);

        CommentEntity commentEntity = commentMapper.toCommentEntity(comment);
        commentEntity.setAuthor(user);
        commentEntity.setAd(ad);
        commentEntity.setCreatedAt(System.currentTimeMillis());

        return commentMapper.toComment(commentRepository.save(commentEntity));
    }

    @Override
    public void deleteComment(String username, int adId, int commentId) {
        UserEntity currentUser = userService.getUserOrThrow(username);
        AdEntity ad = adsService.getAdOrThrow(adId);

        if (!commentRepository.existsById(commentId)) {
            throw new NotFoundException("Comment not found");
        }

        if (currentUser.equals(ad.getAuthor()) || currentUser.getRole().name().equals("ADMIN")) {
            commentRepository.deleteById(commentId);
        }
    }

    @Override
    public Comment updateComment(String username, int adId, int commentId, CreateOrUpdateComment comment) {
        UserEntity currentUser = userService.getUserOrThrow(username);
        AdEntity ad = adsService.getAdOrThrow(adId);

        if (currentUser.equals(ad.getAuthor()) || currentUser.getRole().name().equals("ADMIN")) {
            CommentEntity commentEntity = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment not found"));
            commentEntity.setText(comment.getText());

            return commentMapper.toComment(commentRepository.save(commentEntity));
        } else {
            throw new NoRightsException();
        }
    }
}
