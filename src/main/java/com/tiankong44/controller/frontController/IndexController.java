
package com.tiankong44.controller.frontController;

import com.tiankong44.base.entity.BaseRes;
import com.tiankong44.service.impl.BlogServiceImpl;
import com.tiankong44.service.impl.TagServiceImpl;
import com.tiankong44.util.JsonUtils;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/index")
public class IndexController {
    @Autowired
    private BlogServiceImpl blogService;
    @Autowired
    private TagServiceImpl tagService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());



    /**
     * 获取最新5篇博客
     *
     * @return
     * @author zhanghao_SMEICS
     * @Date 2020/10/24 17:52
     */

    @RequestMapping({"/getNewFiveBlog"})
    @ResponseBody
    public BaseRes getNewFiveBlog() {
        new BaseRes();
        BaseRes res = this.blogService.getFiveNewBlog();
        return res;
    }

    /**
     * 获取最新5条评论的文章
     *
     * @return
     * @author zhanghao_SMEICS
     * @Date 2020/10/24 17:52
     */

    @RequestMapping({"/getNewFiveCommentBlog"})
    @ResponseBody
    public BaseRes getFiveNewCommentBlog() {
        new BaseRes();
        BaseRes res = this.blogService.getFiveNewCommentBlog();
        return res;
    }

    /**
     * 获取浏览量最高的5篇博客
     *
     * @return
     * @author zhanghao_SMEICS
     * @Date 2020/10/24 17:53
     */

    @RequestMapping({"/getTopFiveViewBlog"})
    @ResponseBody
    public BaseRes getTopFiveViewBlog() {
        new BaseRes();
        BaseRes res = this.blogService.getTopFiveViewBlog();
        return res;
    }

    /**
     * 获取博客列表
     *
     * @param msg
     * @return
     * @author zhanghao_SMEICS
     * @Date 2020/10/24 17:54
     */

    @RequestMapping({"/blogList"})
    @ResponseBody
    public BaseRes blogPage(@RequestBody String msg) {
        BaseRes res = new BaseRes();
        JSONObject reqJson = JSONObject.fromObject(msg);
        reqJson = JSONObject.fromObject(msg);
        Map<?, ?> checkMap = JsonUtils.noNulls(reqJson, new String[]{"pageNum", "pageSize"});
        if (checkMap != null) {
            res.setCode(1);
            res.setDesc("请求参数错误");
            return res;
        }
        String title = reqJson.getString("title");
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

    /**
     * 获取首页标签列表
     *
     * @return
     * @author zhanghao_SMEICS
     * @Date 2020/10/24 17:54
     */

    @RequestMapping({"/tagList"})
    @ResponseBody
    public BaseRes getTagList() {
        new BaseRes();
        BaseRes res = this.tagService.getfirstPageTag();
        return res;
    }

    /**
     * 获取智能推荐列表
     * 余弦相似度算法
     *
     * @param request
     * @return
     * @author zhanghao_SMEICS
     * @Date 2020/10/24 17:55
     */

    @RequestMapping({"/recommend"})
    @ResponseBody
    public BaseRes getrecommend(HttpServletRequest request) {
        new BaseRes();
        BaseRes res = this.blogService.getrecommend(request);
        return res;
    }
}
