package com.tiankong44.controller.adminController;

import com.tiankong44.base.entity.BaseRes;
import com.tiankong44.model.User;
import com.tiankong44.service.impl.GalleryServiceImpl;
import com.tiankong44.util.ConstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/admin")
public class GalleryController {
    @Autowired
    private GalleryServiceImpl GalleryServiceImpl;

    @PostMapping("/gallery/deleteAlbumGallery")
    public BaseRes queryAlbumDetail(@RequestBody String msg, HttpServletRequest request) {
        BaseRes res = new BaseRes();
        User user = (User) request.getSession().getAttribute(User.SESSION_KEY);
        if (user == null) {
            res.setCode(ConstantUtil.RESULT_FAILED);
            res.setDesc("请先登录！");
            return res;
        }
        res = GalleryServiceImpl.deleteAlbumGallery(msg, user);
        return res;
    }


}