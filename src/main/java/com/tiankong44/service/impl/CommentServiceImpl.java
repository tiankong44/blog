package com.tiankong44.service.impl;

import com.tiankong44.dao.CommentMapper;
import com.tiankong44.model.Comment;
import com.tiankong44.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName CommentServiceImpl
 * @Description TODO
 * @Author 12481
 * @Date 22:08
 * @Version 1.0
 **/
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Override
    public List<Comment> getCommentByBlogId(Long blogId) {
        List<Comment> commentList = commentMapper.getCommentByBlogId(blogId);

        for (Comment comment : commentList) {
            Long id = comment.getId();
            List<Comment> replyList = commentMapper.getReplyByCommentId(id);
            if (replyList != null) {
                for (Comment reply : replyList) {

                    if (reply.getParentCommentId() == id) {
                        reply.setParentComment(commentMapper.getById(reply.getParentCommentId()));
                        comment.setReplyComments(replyList);
                    }
                }
            }

        }
        return commentList;
    }

    @Override
    public List<Comment> getAllComment() {

        List<Comment> commentList = commentMapper.getAllComment();
    //    List<Comment> replyList = commentMapper.getALLReply();
//        for (Comment comment : commentList) {
//            Long id = comment.getId();
//            List<Comment> replyList = commentMapper.getReplyByCommentId(id);
//            if (replyList != null) {
//                for (Comment reply : replyList) {
//
//                    if (reply.getParentCommentId() == id) {
//                        reply.setParentComment(commentMapper.getById(reply.getParentCommentId()));
//                        comment.setReplyComments(replyList);
//                    }
//                }
//            }
//
//        }
        return commentList;
    }

    @Override
    public List<Comment> getALLReply() {
        return commentMapper.getALLReply();
    }

    public List<Comment> getReplyByBlogId(Long blogId) {
        List<Comment> commentList = commentMapper.getCommentByBlogId(blogId);

        for (Comment comment : commentList) {
            Long id = comment.getId();
            List<Comment> replyList = commentMapper.getReplyByCommentId(id);
            for (Comment reply : replyList) {
                reply.setParentComment(comment);

            }

        }
        return commentList;
    }

    @Override
    public List<Comment> getFiveNewComment() {
        return commentMapper.getFiveNewComment();
    }

    @Override
    public List<Comment> getReplyByCommentId(Long id) {
        return commentMapper.getReplyByCommentId(id);
    }

    @Override
    public void saveComment(Comment comment) {

        Long parentCommentId = comment.getParentCommentId();
        if (parentCommentId != -1) {
            commentMapper.saveReply(comment);
        } else {
            comment.setParentComment(null);
            comment.setParentCommentId(null);
            commentMapper.saveComment(comment);
        }
    }

}
