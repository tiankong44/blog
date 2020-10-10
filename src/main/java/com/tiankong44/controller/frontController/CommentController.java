package com.tiankong44.controller.frontController;

import com.tiankong44.model.Comment;
import com.tiankong44.model.User;
import com.tiankong44.service.BlogService;
import com.tiankong44.service.CommentService;
import com.tiankong44.util.AvatarUtil;
import com.tiankong44.util.QiniuUpload;
import org.apache.commons.fileupload.FileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Date;
import java.util.UUID;

/**
 * @ClassName CommentController
 * @Description TODO
 * @Author 12481
 * @Date 21:56
 * @Version 1.0
 **/
@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private BlogService blogService;
    private String avatar = "http://images.tiankong44.vip/1583320687435";
    private String src = "E:\\ideaIC-2019.3.4.win\\MyProject\\myblog\\src\\main\\resources\\static\\images\\avatar.jpg";

    @PostMapping("/comments")
    public String save(@Param("blogId") Long blogId, @Param("parentCommentId") Long parentCommentId,
                       @Param("content") String content,
                       @Param("nickname") String nickname,
                       @Param("email") String email, HttpSession session) throws Exception {
        Comment comment = new Comment();
        User user = (User) session.getAttribute("user");
        if (user != null) {
            comment.setAvatar(user.getAvatar());
            comment.setAdmin(true);
        } else {
            //设置头像
            AvatarUtil.avatarAddText(src, nickname);
            File avatarImage = new File("E:\\ideaIC-2019.3.4.win\\MyProject\\myblog\\src\\main\\resources\\static\\images\\out.jpg");

            // FileItemFactory factory = new DiskFileItemFactory(16, null);
            FileItem item = AvatarUtil.createFileItem(avatarImage, avatarImage.getName());//factory.createItem(avatarImage.getName(), "text/plain", true, avatarImage.getName());
            MultipartFile multipartFile = new CommonsMultipartFile(item);
            String uuid = UUID.randomUUID().toString().replaceAll("-","");
            String path = QiniuUpload.updateFile(multipartFile, uuid);
            comment.setAvatar(path);
            comment.setAdmin(false);
        }
        comment.setParentCommentId(parentCommentId);
        comment.setBlogId(blogId);
        comment.setBlog(blogService.getBlogById(blogId));
        comment.setEmail(email);
        comment.setPublished(true);
        comment.setCreateTime(new Date());
        comment.setNickname(nickname);
        if ("".equals(content)) {
            return "redirect:/blog/" + blogId;

        } else {
            comment.setContent(content);

            //  System.out.println(comment.toString());
            commentService.saveComment(comment);
            return "redirect:/blog/" + blogId;
        }
    }
}
