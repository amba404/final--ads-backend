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
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.service.UserService;

/**
 * Реализация сервиса для работы с комментариями
 */
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final AdsService adsService;
    private final AdsRepository adsRepository;
    private final CommentMapper commentMapper;

    /**
     * Метод получения комментариев по id объявления
     * @param adId  id объявления
     * @return DTO {@link Comments}
     * @throws NotFoundException если объявление не найдено
     */
    @Override
    public Comments getComments(int adId) {
        if (!adsRepository.existsById(adId)) {
            throw new NotFoundException("Ad not found");
        }
        return commentMapper.toComments(commentRepository.findAllByAdId(adId));
    }

    /**
     * Метод добавления комментария
     * @param username логин пользователя
     * @param adId id объявления
     * @param comment DTO {@link CreateOrUpdateComment} структура с информацией о добавляемом комментарии
     * @return DTO {@link Comment}
     * @throws NotFoundException если объявление не найдено или пользователь не найден
     */
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

    /**
     * Метод удаления комментария
     * @param username логин пользователя
     * @param adId id объявления
     * @param commentId id комментария
     * @throws NotFoundException если объявление не найдено или комментарий не найден
     * @throws NoRightsException если пользователь не является владельцем комментария и не админ
     */
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

    /**
     * Метод обновления комментария
     * @param username логин пользователя
     * @param adId id объявления
     * @param commentId id комментария
     * @param comment DTO {@link CreateOrUpdateComment} структура с информацией о комментарии
     * @return DTO {@link Comment}
     * @throws NotFoundException если объявление не найдено или комментарий не найден
     * @throws NoRightsException если пользователь не является владельцем комментария и не админ
     */
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

    /**
     * Метод получения комментария по id
     * @param commentId id комментария
     * @return DTO {@link Comment}
     * @throws NotFoundException если комментарий не найден
     */
    @Override
    public CommentEntity getCommentOrThrow(int commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found"));
    }
}
