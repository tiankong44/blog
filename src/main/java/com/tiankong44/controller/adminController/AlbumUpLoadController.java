package com.tiankong44.controller.adminController;


import com.tiankong44.model.Album;
import com.tiankong44.service.impl.AlbumServiceImpl;
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
public class AlbumUpLoadController {
    @Autowired
    private Album album;
    @Autowired
    private AlbumServiceImpl albumService;

    @PostMapping("/uploadToAlbum")
    @ResponseBody
    public Map<String, Object> upload(@Param("img") MultipartFile[] img,
                                      HttpServletRequest request) throws Exception {
        String path = "";
        Long userId = (Long) request.getSession().getAttribute("user_id");
        for (MultipartFile mf : img) {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            path = QiniuUpload.updateFile(mf, uuid);
            album.setImgName(uuid);
            album.setPath(path);
            Date date = new Date();
            album.setUploadDate(date.toString());
            album.setUserId(userId);
            albumService.saveImg(album);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("msg", "ok");
        Map<String, Object> data = new HashMap<>();
        data.put("src", path);
        map.put("data", data);
        return map;
    }


}
