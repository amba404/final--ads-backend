package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
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
    public Comments getComments(int adId) {
        if (!commentRepository.existsByAdId(adId)) {
            throw new NotFoundException("Ad not found");
        }
        return commentMapper.toComments(commentRepository.findAllByAdId(adId));
    }

    @Override
    public Comment addComment(String username, int adId, CreateOrUpdateComment comment) {
        UserEntity user = userService.getUserOrThrow(username);
        AdEntity ad = adsService.getAdOrThrow(adId);

        CommentEntity commentEntity = new CommentEntity();

        commentEntity.setAuthor(user);
        commentEntity.setText(comment.getText());
        commentEntity.setCreatedAt(System.currentTimeMillis());
        commentEntity.setAd(ad);

        return commentMapper.toComment(commentRepository.save(commentEntity));
    }

    @Override
    public void deleteComment(String username, int adId, int commentId) {
        if (!commentRepository.existsByAdId(adId)) {
            throw new NotFoundException("Ad not found");
        }

        CommentEntity comment = commentRepository.findById(commentId).orElse(null);

        if (comment == null) {
            return;
        }

        userService.checkOwnerOrThrow(username, comment.getAuthor());

        commentRepository.delete(comment);
    }

    @Override
    public Comment updateComment(String username, int adId, int commentId, CreateOrUpdateComment comment) {
        if (!commentRepository.existsByAdId(adId)) {
            throw new NotFoundException("Ad not found");
        }

        CommentEntity commentEntity = getCommentOrThrow(commentId);

        userService.checkOwnerOrThrow(username, commentEntity.getAuthor());

        commentEntity.setText(comment.getText());

        return commentMapper.toComment(commentRepository.save(commentEntity));
    }

    @Override
    public CommentEntity getCommentOrThrow(int commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found"));
    }
}
