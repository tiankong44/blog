//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.tiankong44.controller.frontController;

import com.tiankong44.base.entity.BaseRes;
import com.tiankong44.model.User;
import com.tiankong44.service.BlogService;
import com.tiankong44.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/blog")
public class BlogController {
    @Autowired
    BlogService blogService;
    @Autowired
    private CommentService commentService;

    public BlogController() {
    }

    /**
     * 查看博客详情
     *
     * @param request
     * @param msg
     * @return
     * @author zhanghao_SMEICS
     * @Date 2020/10/24 17:58
     */

    @RequestMapping({"/blogDetail"})
    @ResponseBody
    public BaseRes blog(HttpSession session, HttpServletRequest request, @RequestBody String msg) {
        new BaseRes();
        BaseRes res = this.blogService.getBlogAndConvert(session, msg, request);
        return res;
    }

    /**
     * 点赞
     *
     * @param request
     * @param msg
     * @return
     * @author zhanghao_SMEICS
     * @Date 2020/10/24 17:58
     */

    @RequestMapping({"/praise"})
    @ResponseBody
    public BaseRes updatePraise(HttpServletRequest request, @RequestBody String msg) {
        new BaseRes();
        BaseRes res = this.blogService.updateBlogPraise(request, msg);
        return res;

    }

    /**
     * 获取评论列表
     *
     * @param request
     * @param msg
     * @return
     * @author zhanghao_SMEICS
     * @Date 2020/10/24 17:59
     */

    @RequestMapping({"/getComments"})
    @ResponseBody
    public BaseRes getComment(HttpServletRequest request, @RequestBody String msg) {
        new BaseRes();
        BaseRes res = this.blogService.getComment(msg);
        return res;

    }


    /**
     * 保存评论
     *
     * @param request
     * @param msg
     * @return
     * @author zhanghao_SMEICS
     * @Date 2020/10/24 18:04
     */

    @PostMapping("/comments")
    @ResponseBody
    public BaseRes save(HttpServletRequest request, @RequestBody String msg) throws Exception {
        BaseRes res = new BaseRes();
//        User user = (User) session.getAttribute("user");
        User user = (User) request.getSession().getAttribute(User.SESSION_KEY);
        res = commentService.saveComment(user, msg);
        return res;
    }
}
