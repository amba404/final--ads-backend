package ru.skypro.homework.service;

import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;

public interface CommentService {

    Comments getComments(int id);

    Comment addComment(String username, int id, CreateOrUpdateComment comment);

    void deleteComment(String username, int adId, int commentId);

    Comment updateComment(String username, int adId, int commentId, CreateOrUpdateComment comment);

}
