package com.tiankong44.dao;

import com.tiankong44.model.Blog;
import com.tiankong44.model.Comment;
import net.sf.json.JSONObject;
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

    void saveComment(JSONObject reqJson);

    void saveReply(JSONObject reqJson);

    Comment getByParentCommentId(Long parentCommentId);

    List<Blog> getFiveNewComment();

    Comment getById(Long id);

    /**
     * 优化后的获取评论列表
     *
     * @param blogId
     * @author zhanghao_SMEICS
     * @Date 2020/10/23 11:10
     * @return
     */

    List<Comment> getCommentsByBlogId(Long blogId);
}
