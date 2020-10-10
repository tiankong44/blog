package com.tiankong44.controller.frontController;

import com.tiankong44.model.Message;
import com.tiankong44.model.User;
import com.tiankong44.service.CommentService;
import com.tiankong44.service.MessageService;
import com.tiankong44.util.AvatarUtil;
import com.tiankong44.util.QiniuUpload;
import org.apache.commons.fileupload.FileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping
public class MessageController {
    private String avatar = "http://images.tiankong44.vip/1583320687435";
    @Autowired
    private CommentService commentService;
    @Autowired
    private MessageService messageService;

    private String src = "E:\\ideaIC-2019.3.4.win\\MyProject\\myblog\\src\\main\\resources\\static\\images\\avatar.jpg";


    public MessageController() throws FileNotFoundException {
    }

    @GetMapping("/message")
    public String tag(Model model) {
        model.addAttribute("comments", messageService.getAllMessage());
        List<Message> messageList = messageService.getAllMessage();
        return "message";
    }

    @PostMapping("/messages")
    public String save(@Param("parentCommentId") Long parentCommentId,
                       @Param("content") String content,
                       @Param("nickname") String nickname,
                       @Param("email") String email, HttpSession session) throws Exception {

        Message message = new Message();
        User user = (User) session.getAttribute("user");
        if (user != null) {
            message.setAvatar(user.getAvatar());
            message.setAdmin(true);
        } else {
            //设置头像
            AvatarUtil.avatarAddText(src, nickname);
            File avatarImage = new File("E:\\ideaIC-2019.3.4.win\\MyProject\\myblog\\src\\main\\resources\\static\\images\\out.jpg");
            // FileItemFactory factory = new DiskFileItemFactory(16, null);
            FileItem item = AvatarUtil.createFileItem(avatarImage, avatarImage.getName());//factory.createItem(avatarImage.getName(), "text/plain", true, avatarImage.getName());
            MultipartFile multipartFile = new CommonsMultipartFile(item);
            String uuid = UUID.randomUUID().toString().replaceAll("-","");
            String path = QiniuUpload.updateFile(multipartFile, uuid);
            message.setAvatar(path);
            message.setAdmin(false);
        }
        message.setParentCommentId(parentCommentId);
        message.setEmail(email);
        message.setPublished(true);
        message.setCreateTime(new Date());
        message.setNickname(nickname);
        if ("".equals(content)) {
            return "redirect:/message/";
        } else {
            message.setContent(content);
            messageService.saveMessage(message);
            return "redirect:/message/";
        }
    }
}
