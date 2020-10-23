package com.tiankong44.controller.frontController;

import com.tiankong44.base.entity.BaseRes;
import com.tiankong44.model.User;
import com.tiankong44.service.BlogService;
import com.tiankong44.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
    private String src = "E:\\GitRepo\\blog\\src\\main\\resources\\static\\images\\avatar.jpg";

    @PostMapping("/comments")
    @ResponseBody
    public BaseRes save(HttpServletRequest request, @RequestBody String msg, HttpSession session) throws Exception {
        BaseRes res = new BaseRes();

        User user = (User) session.getAttribute("user");
        res = commentService.saveComment(user, msg);

        return res;
    }
}
