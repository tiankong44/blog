//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.tiankong44.controller.frontController;

import com.tiankong44.base.entity.BaseRes;
import com.tiankong44.model.Blog;
import com.tiankong44.service.BlogService;
import com.tiankong44.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping
public class BlogController {
    @Autowired
    BlogService blogService;
    @Autowired
    private CommentService commentService;

    public BlogController() {
    }

    @RequestMapping({"/blog"})
    @ResponseBody
    public BaseRes blog(HttpSession session, HttpServletRequest request, @RequestBody String msg) {
        new BaseRes();
        BaseRes res = this.blogService.getBlogAndConvert(session, msg, request);
        return res;
    }

    @RequestMapping({"/blog/praise/{id}"})
    @ResponseBody
    public Map<String, Object> updatePraise(@PathVariable Long id, Model model, HttpServletRequest request, HttpSession session) {
        String ip = request.getRemoteAddr() + id;
        Blog blog;
        HashMap data;
        if (session.getAttribute("praiseIp") != null && session.getAttribute("praiseIp").equals(ip)) {
            session.setAttribute("praiseIp", ip);
            blog = this.blogService.getBlogById(id);
            data = new HashMap();
            data.put("praise", blog.getPraise());
            data.put("message", "你已经点过赞了！");
            model.addAttribute("blog", blog);
            return data;
        } else {
            this.blogService.updateBlogPraise(id);
            session.setAttribute("praiseIp", ip);
            blog = this.blogService.getBlogById(id);
            data = new HashMap();
            data.put("praise", blog.getPraise());
            data.put("message", "点赞成功！");
            model.addAttribute("blog", blog);
            return data;
        }
    }
}
