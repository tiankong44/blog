package com.tiankong44.controller.frontController;

import com.tiankong44.base.entity.BaseRes;
import com.tiankong44.service.BlogService;
import com.tiankong44.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tag")
public class TagController {
    @Autowired
    private TagService tagService;
    @Autowired
    private BlogService blogService;

    /**
     * 获取标签列表
     *
     * @param request
     * @param msg
     * @return
     * @author zhanghao_SMEICS
     * @Date 2020/10/24 17:59
     */

    @RequestMapping({"/getTags"})
    @ResponseBody
    public BaseRes getTags() {
        BaseRes res = new BaseRes();
        res = this.tagService.getAdminTag();
        return res;

    }

    /**
     * 根据标签获取博客列表
     *
     * @param request
     * @param msg
     * @return
     * @author zhanghao_SMEICS
     * @Date 2020/10/24 17:59
     */

    @RequestMapping({"/getBlogList"})
    @ResponseBody
    public BaseRes getBlogList(HttpServletRequest request, @RequestBody String msg) {
        BaseRes res = new BaseRes();
        res = this.blogService.getBlogListByTagId(msg);
        return res;

    }

}
