package com.tiankong44.controller.frontController;

import com.tiankong44.service.BlogService;
import com.tiankong44.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

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

}
