package com.tiankong44.controller.adminController;


import com.tiankong44.base.entity.BaseRes;
import com.tiankong44.model.Gallery;
import com.tiankong44.model.User;
import com.tiankong44.service.impl.GalleryServiceImpl;
import com.tiankong44.util.ConstantUtil;
import com.tiankong44.util.QiniuUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class GalleryUpLoadController {
    @Autowired
    private Gallery Gallery;
    @Autowired
    private GalleryServiceImpl galleryServiceImpl;

    @PostMapping("/uploadToGallery")
    @ResponseBody
    public BaseRes upload(@RequestParam("files") MultipartFile[] file,
                          HttpServletRequest request) throws Exception {
        BaseRes res = new BaseRes();
        String path = "";
        User user = (User) request.getSession().getAttribute(User.SESSION_KEY);
        Long userId = user.getId();
        for (MultipartFile mf : file) {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            path = QiniuUpload.updateFile(mf, uuid);
            Gallery.setImgName(uuid);
            Gallery.setPath(path);
            Date date = new Date();
            Gallery.setUploadDate(date);
            Gallery.setUserId(userId);
            boolean flag = galleryServiceImpl.saveImg(Gallery);
            if (!flag) {
                res.setCode(ConstantUtil.RESULT_FAILED);
                res.setDesc("图片上传失败！");

            }
        }
        res.setCode(ConstantUtil.RESULT_SUCCESS);
        res.setDesc("图片上传成功！");
        res.setData(path);
        return res;
    }
}
