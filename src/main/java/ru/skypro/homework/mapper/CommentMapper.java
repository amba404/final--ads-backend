package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.model.CommentEntity;
import ru.skypro.homework.model.ImageEntity;
import ru.skypro.homework.model.UserEntity;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Маппер для CommentEntity
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentMapper {

    /**
     * Конвертирует CommentEntity в Comment
     * @param comment сущность комментария {@link CommentEntity}
     * @return DTO {@link Comment}
     */
    default Comment toComment(CommentEntity comment) {
        Comment result = new Comment();

        result.setPk(comment.getId());
        result.setCreatedAt(comment.getCreatedAt());
        result.setText(comment.getText());

        UserEntity user = comment.getAuthor();

        if (user == null) {
            throw new IllegalArgumentException("User is null");
        } else {
            result.setAuthor(user.getId());
            result.setAuthorFirstName(user.getFirstName());
            ImageEntity image = user.getImage();
            if (image != null) {
                result.setAuthorImage(image.getUrl());
            }
        }

        return result;
    }

    /**
     * Конвертирует список CommentEntity в Comments
     * @param comments список сущностей {@link List} < {@link CommentEntity} >
     * @return DTO {@link Comments}
     */
    default Comments toComments(List<CommentEntity> comments) {
        Comments result = new Comments();

        if (comments == null || comments.isEmpty()) {
            result.setCount(0);
            result.setResults(List.of());
        } else {
            result.setCount(comments.size());
            List<Comment> commentsDtoList = comments.stream().map(this::toComment).collect(Collectors.toList());
            result.setResults(commentsDtoList);
        }

        return result;
    }
}
