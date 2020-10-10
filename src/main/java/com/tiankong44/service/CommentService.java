package com.tiankong44.service;

import com.tiankong44.model.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> getCommentByBlogId(Long blogId);

    void saveComment(Comment comment);

    List<Comment> getFiveNewComment();

    List<Comment> getReplyByCommentId(Long id);

    List<Comment> getAllComment();

    List<Comment> getALLReply();
}
