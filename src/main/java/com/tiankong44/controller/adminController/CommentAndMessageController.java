package com.tiankong44.controller.adminController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tiankong44.model.Comment;
import com.tiankong44.service.CommentService;
import com.tiankong44.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @ClassName CommentAndMessageController
 * @Description TODO
 * @Author 12481
 * @Date 15:54
 * @Version 1.0
 **/

@Controller
@RequestMapping("/admin")

public class CommentAndMessageController {
    @Autowired
    private CommentService commentService;
    private MessageService messageService;


    @RequestMapping("/commentAndMessage")
    public String toCommentAndMessage(Model model,
                                      @RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum) {
        PageHelper.startPage(pageNum, 10);
       List<Comment> commentList= commentService.getAllComment();
        PageInfo<Comment> pageInfo = new PageInfo<>(commentList);
        model.addAttribute("pageInfo", pageInfo);
        return "/admin/comment";
    }
}
