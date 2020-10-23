package com.tiankong44.service.impl;

import cn.hutool.core.date.DateUtil;
import com.tiankong44.base.entity.BaseRes;
import com.tiankong44.dao.CommentMapper;
import com.tiankong44.model.Comment;
import com.tiankong44.model.User;
import com.tiankong44.service.CommentService;
import com.tiankong44.util.AvatarUtil;
import com.tiankong44.util.ConstantUtil;
import com.tiankong44.util.JsonUtils;
import com.tiankong44.util.QiniuUpload;
import net.sf.json.JSONObject;
import org.apache.commons.fileupload.FileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    private String src = "E:\\GitRepo\\blog\\src\\main\\resources\\static\\images\\avatar.jpg";

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
    public BaseRes saveComment(User user, String msg) {
        BaseRes res = new BaseRes();
        Comment comment = new Comment();
        JSONObject reqJson = null;
        try {
            reqJson = JSONObject.fromObject(msg);
            Map<?, ?> checkMap = JsonUtils.noNulls(reqJson, "blogId", "nickname", "email", "content", "parentCommentId");
            if (checkMap != null) {
                res.setCode(1);
                res.setDesc("请求参数错误");
                return res;
            }
            if (!reqJson.containsKey("nickname")) {
                res.setCode(1);
                res.setDesc("请输入昵称");
                return res;
            }
            if (!reqJson.containsKey("email")) {
                res.setCode(1);
                res.setDesc("请输入邮箱");
                return res;
            }
            if (!reqJson.containsKey("content")) {
                res.setCode(1);
                res.setDesc("请输入评论内容");
                return res;
            }
            Long blogId = reqJson.getLong("blogId");
            String nickname = reqJson.getString("nickname");
            String email = reqJson.getString("email");
            String content = reqJson.getString("content");
            Long parentCommentId = reqJson.getLong("parentCommentId");


            if (user != null) {
                comment.setAvatar(user.getAvatar());
                reqJson.put("admin", 1);
            } else {
                //设置头像
                AvatarUtil.avatarAddText(src, nickname);
                File avatarImage = new File("E:\\GitRepo\\blog\\src\\main\\resources\\static\\images\\out.jpg");

                FileItem item = AvatarUtil.createFileItem(avatarImage, avatarImage.getName());//factory.createItem(avatarImage.getName(), "text/plain", true, avatarImage.getName());
                MultipartFile multipartFile = new CommonsMultipartFile(item);
                String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                String path = QiniuUpload.updateFile(multipartFile, uuid);
                reqJson.put("avatar", path);
                reqJson.put("admin", 0);
            }
            comment.setParentCommentId(parentCommentId);
//            comment.setBlogId(blogId);
//            comment.setBlog(blogService.getBlogById(blogId));
//            comment.setEmail(email);
//            comment.setPublished(1);
            reqJson.put("published", 1);

            String formatDateTime = DateUtil.formatDateTime(new Date());
            reqJson.put("createTime", formatDateTime);
//            comment.setCreateTime(formatDateTime);
//            comment.setNickname(nickname);
//            comment.setContent(content);

            if (parentCommentId != -1) {
                commentMapper.saveReply(reqJson);
            } else {
//                comment.setParentComment(null);
//                comment.setParentCommentId(null);
                reqJson.put("parentCommentId", null);
                commentMapper.saveComment(reqJson);
            }
            res.setCode(ConstantUtil.RESULT_SUCCESS);
            res.setDesc("评论成功");

        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(1);
            res.setDesc("请求参数异常");
            return res;
        }

        return res;
    }

}
