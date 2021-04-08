//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.tiankong44.controller.adminController;

import com.qiniu.common.QiniuException;
import com.tiankong44.base.entity.BaseRes;
import com.tiankong44.service.impl.BlogServiceImpl;
import com.tiankong44.service.impl.TagServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping({"/admin"})
public class BlogsController {
    @Autowired
    private BlogServiceImpl blogService;
    @Autowired
    private TagServiceImpl tagService;

    public BlogsController() {
    }

    @PostMapping({"/blog/queryBlogList"})
    public BaseRes getBlogList(@RequestBody String msg, HttpServletRequest request) {

        BaseRes res = new BaseRes();
        res = blogService.queryBlogList(msg, request);
        return res;
    }

    @RequestMapping({"/blog/save"})
    public BaseRes blogsSave(@RequestBody String msg, HttpServletRequest request) {
        BaseRes res = new BaseRes();
        res = blogService.saveBlog(msg, request);
        return res;
    }

    @PostMapping({"/blog/delete"})
    public BaseRes delete(@RequestBody String msg, HttpServletRequest request) throws QiniuException {
        BaseRes res = new BaseRes();
        res = blogService.deleteBlog(msg);
        return res;
    }

    @PostMapping("/blog/blogDetail")
    public BaseRes blogDetail(@RequestBody String msg, HttpServletRequest request) {
        BaseRes res = new BaseRes();
        res = this.blogService.getBlogDetail(msg, request);
        return res;
    }


    @PostMapping({"/blog/update"})
    public BaseRes update(@RequestBody String msg, HttpServletRequest request) {
        BaseRes res = new BaseRes();
        res = this.blogService.updateBlog(msg, request);
        return res;
    }
}
