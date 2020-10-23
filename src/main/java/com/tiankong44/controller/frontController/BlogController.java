//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.tiankong44.controller.frontController;

import com.tiankong44.base.entity.BaseRes;
import com.tiankong44.service.BlogService;
import com.tiankong44.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping
public class BlogController {
    @Autowired
    BlogService blogService;
    @Autowired
    private CommentService commentService;

    public BlogController() {
    }

    @RequestMapping({"/blogDetail"})
    @ResponseBody
    public BaseRes blog(HttpSession session, HttpServletRequest request, @RequestBody String msg) {
        new BaseRes();
        BaseRes res = this.blogService.getBlogAndConvert(session, msg, request);
        return res;
    }

    @RequestMapping({"/praise"})
    @ResponseBody
    public BaseRes updatePraise(HttpServletRequest request, @RequestBody String msg) {
        new BaseRes();
        BaseRes res = this.blogService.updateBlogPraise(request, msg);
        return res;

    }

    @RequestMapping({"/getComments"})
    @ResponseBody
    public BaseRes getComment(HttpServletRequest request, @RequestBody String msg) {
        new BaseRes();
        BaseRes res = this.blogService.getComment(msg);
        return res;

    }
}
