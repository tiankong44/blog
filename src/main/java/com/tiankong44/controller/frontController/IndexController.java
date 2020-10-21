
package com.tiankong44.controller.frontController;

import com.tiankong44.base.entity.BaseRes;
import com.tiankong44.service.impl.BlogServiceImpl;
import com.tiankong44.service.impl.TagServiceImpl;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping
public class IndexController {
    @Autowired
    private BlogServiceImpl blogService;
    @Autowired
    private TagServiceImpl tagService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public IndexController() {
    }

    @RequestMapping({"/getTopFiveBlog"})
    @ResponseBody
    public BaseRes getTopFiveBlog() {
        new BaseRes();
        BaseRes res = this.blogService.getTopFiveViewBlog();
        return res;
    }

    @RequestMapping({"/getNewFiveBlog"})
    @ResponseBody
    public BaseRes getNewFiveBlog() {
        new BaseRes();
        BaseRes res = this.blogService.getFiveNewBlog();
        return res;
    }

    @RequestMapping({"/getNewFiveCommentBlog"})
    @ResponseBody
    public BaseRes getFiveNewCommentBlog() {
        new BaseRes();
        BaseRes res = this.blogService.getFiveNewCommentBlog();
        return res;
    }

    @RequestMapping({"/getTopFiveViewBlog"})
    @ResponseBody
    public BaseRes getTopFiveViewBlog() {
        new BaseRes();
        BaseRes res = this.blogService.getTopFiveViewBlog();
        return res;
    }

    @RequestMapping({"/blogList"})
    @ResponseBody
    public BaseRes blogPage(@RequestBody String msg) {
        new BaseRes();
        JSONObject reqJson = JSONObject.fromObject(msg);
        String title = reqJson.getString("title");
        BaseRes res;
        if (!"".equals(title) && title != null) {
            res = this.blogService.getFirstPageSearch(msg);
        } else {
            String tagId = reqJson.getString("tagId");
            if ("0".equals(tagId)) {
                res = this.blogService.getFirstPageBlog(msg);
            } else {
                res = this.blogService.getBlogListByTagId(msg);
            }
        }

        return res;
    }

    @RequestMapping({"/tagList"})
    @ResponseBody
    public BaseRes getTagList() {
        new BaseRes();
        BaseRes res = this.tagService.getfirstPageTag();
        return res;
    }

    @RequestMapping({"/recommend"})
    @ResponseBody
    public BaseRes getrecommend(HttpServletRequest request) {
        new BaseRes();
        BaseRes res = this.blogService.getrecommend(request);
        return res;
    }
}
