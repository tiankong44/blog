package com.tiankong44.controller.adminController;

import com.tiankong44.base.entity.BaseRes;
import com.tiankong44.model.User;
import com.tiankong44.service.impl.TagServiceImpl;
import com.tiankong44.util.ConstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/admin")
public class TagsController {
    @Autowired
    private TagServiceImpl tagService;

    @PostMapping("/tag/getTagsByUserId")
    public BaseRes getTagsByUserId(HttpServletRequest request) {
        BaseRes res = new BaseRes();
        User user = (User) request.getSession().getAttribute(User.SESSION_KEY);
        if (user == null) {
            res.setCode(ConstantUtil.RESULT_FAILED);
            res.setDesc("请先登录！");
            return res;
        }
        Long userId = user.getId();
        res = tagService.getTagsByUserId(userId);
        return res;
    }

    @PostMapping("/tag/queryTagList")
    public BaseRes queryTagList(HttpServletRequest request, @RequestBody String msg) {
        BaseRes res = new BaseRes();
        User user = (User) request.getSession().getAttribute(User.SESSION_KEY);
        if (user == null) {
            res.setCode(ConstantUtil.RESULT_FAILED);
            res.setDesc("请先登录！");
            return res;
        }
        res = tagService.queryTagList(msg, user);
        return res;
    }

    //删除标签
    @PostMapping("/tag/delete")
    public BaseRes delete(HttpServletRequest request, @RequestBody String msg) {
        BaseRes res = new BaseRes();
        User user = (User) request.getSession().getAttribute(User.SESSION_KEY);
        if (user == null) {
            res.setCode(ConstantUtil.RESULT_FAILED);
            res.setDesc("请先登录！");
            return res;
        }
        res = tagService.deleteTag(msg, user);
        return res;
    }


    @PostMapping("/tag/add")
    public BaseRes add(HttpServletRequest request, @RequestBody String msg) {
        BaseRes res = new BaseRes();
        User user = (User) request.getSession().getAttribute(User.SESSION_KEY);
        if (user == null) {
            res.setCode(ConstantUtil.RESULT_FAILED);
            res.setDesc("请先登录！");
            return res;
        }
        res = tagService.addTag(msg,user);
        return res;
    }
    @PostMapping("/tag/update")
    public BaseRes update(HttpServletRequest request, @RequestBody String msg) {
        BaseRes res = new BaseRes();
        User user = (User) request.getSession().getAttribute(User.SESSION_KEY);
        if (user == null) {
            res.setCode(ConstantUtil.RESULT_FAILED);
            res.setDesc("请先登录！");
            return res;
        }
        res = tagService.updateTag(msg,user);
        return res;
    }


}