package com.tiankong44.service;

import com.tiankong44.base.entity.BaseRes;
import com.tiankong44.model.Comment;
import com.tiankong44.model.User;

import java.util.List;

public interface CommentService {
    List<Comment> getCommentByBlogId(Long blogId);

    BaseRes saveComment(User user, String msg);

    List<Comment> getFiveNewComment();

    List<Comment> getReplyByCommentId(Long id);

    List<Comment> getAllComment();

    List<Comment> getALLReply();
}
