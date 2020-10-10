package com.tiankong44.controller.frontController;

import com.tiankong44.model.Blog;
import com.tiankong44.model.Tag;
import com.tiankong44.service.BlogService;
import com.tiankong44.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping
public class BlogController {
    @Autowired
    BlogService blogService;
    @Autowired
    private CommentService commentService;

    @RequestMapping("/blog/{id}")
    public String blog(@PathVariable Long id, Model model, HttpSession session, HttpServletRequest request) {

        String ip = request.getRemoteAddr() + id;
        if (session.getAttribute("ip") != null && session.getAttribute("ip").equals(ip)) {
            // blogService.updateBlogViews(id);
            session.setAttribute("ip", ip);
        } else {
            session.setAttribute("ip", ip);

            blogService.updateBlogViews(id);
        }
        Blog blog = blogService.getBlogAndConvert(id);
//博客推荐根据title

        StringBuffer blogTitle = new StringBuffer();
        blogTitle.append(session.getAttribute("blogTitle"));
        session.setAttribute("blogTitle", blogTitle.append(blog.getTitle()) + "-");

        //List<String> titleWord = WordSegment.TextToWord(blogTitle);
        //  System.out.println(titleWord.toString());


//博客推荐根据标签
        StringBuffer blogTag = new StringBuffer();
        blogTag.append(session.getAttribute("blogTag"));
        List<Tag> tags = blog.getTags();
        for (Tag tag : tags) {
            session.setAttribute("blogTag", blogTag.append(tag.getName()) + ",");
        }
        // List<String> tagWord = WordSegment.TextToWord(blogTag);
        // System.out.println(tagWord.toString());
//博客推荐根据描述
        StringBuffer blogDesc = new StringBuffer();
        blogDesc.append(session.getAttribute("blogDesc"));
        session.setAttribute("blogDesc", blogDesc.append(blog.getDescription()) + "-");
        //  List<String> descWord = WordSegment.TextToWord(blogDesc);
        // System.out.println(descWord.toString());
        model.addAttribute("comments", commentService.getCommentByBlogId(id));
        model.addAttribute("blog", blog);
        session.setAttribute("id", id);
        return "blog";
    }

    @RequestMapping("/blog/praise/{id}")
    @ResponseBody
    public Map<String, Object> updatePraise(@PathVariable Long id, Model model, HttpServletRequest request, HttpSession session) {
        String ip = request.getRemoteAddr() + id;
        if (session.getAttribute("praiseIp") != null && session.getAttribute("praiseIp").equals(ip)) {
            session.setAttribute("praiseIp", ip);
            Blog blog = blogService.getBlogById(id);
            Map<String, Object> data = new HashMap<>();
            // String views=blog.getViews().toString();
            // String message="你已经点过赞了！";
            data.put("praise", blog.getPraise());
            data.put("message", "你已经点过赞了！");
            model.addAttribute("blog", blog);
            return data;
        } else {
            blogService.updateBlogPraise(id);
            session.setAttribute("praiseIp", ip);
            Blog blog = blogService.getBlogById(id);
            Map<String, Object> data = new HashMap<>();
            data.put("praise", blog.getPraise());
            data.put("message", "点赞成功！");
            model.addAttribute("blog", blog);
            return data;
        }
    }


}
