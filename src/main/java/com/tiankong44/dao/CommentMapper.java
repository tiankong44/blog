package com.tiankong44.dao;

import com.tiankong44.model.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName CommentMapper
 * @Description TODO
 * @Author 12481
 * @Date 22:10
 * @Version 1.0
 **/
@Repository
@Mapper
public interface CommentMapper {
    List<Comment> getCommentByBlogId(Long blogId);

    List<Comment> getReplyByCommentId(Long id);

    List<Comment> getAllComment();

    List<Comment> getALLReply();

    void saveComment(Comment comment);

    void saveReply(Comment comment);

    Comment getByParentCommentId(Long parentCommentId);

    List<Comment> getFiveNewComment();

    Comment getById(Long id);
}
