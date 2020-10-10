package com.tiankong44.controller.adminController;


import com.tiankong44.model.Gallery;
import com.tiankong44.service.impl.GalleryServiceImpl;
import com.tiankong44.util.QiniuUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
    public /**String*/
    Map<String, Object> upload(@Param("file") MultipartFile[] file,
                               HttpServletRequest request) throws Exception {
        String path="";
        Long userId = (Long) request.getSession().getAttribute("user_id");
        for (MultipartFile mf : file) {
            String uuid = UUID.randomUUID().toString().replaceAll("-","");
           path = QiniuUpload.updateFile(mf, uuid);
            Gallery.setImgName(uuid);
            Gallery.setPath(path);
            Date date = new Date();
            Gallery.setUploadDate(date);
            Gallery.setUserId(userId);
            galleryServiceImpl.saveImg(Gallery);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "操作成功！");

        map.put("url", path);

        return map;

    }


}
