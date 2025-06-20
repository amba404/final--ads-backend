package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {

    @Override
    public Comments getComments(int id) {
        return null;
    }

    @Override
    public Comment addComment(int id, CreateOrUpdateComment comment) {
        return null;
    }

    @Override
    public boolean deleteComment(int adId, int commentId) {
        return false;
    }

    @Override
    public Comment updateComment(int adId, int commentId, CreateOrUpdateComment comment) {
        return null;
    }

}
