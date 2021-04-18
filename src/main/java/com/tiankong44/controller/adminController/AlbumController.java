package com.tiankong44.controller.adminController;

import cn.hutool.core.date.DateUtil;
import com.tiankong44.base.entity.BaseRes;
import com.tiankong44.model.Gallery;
import com.tiankong44.model.User;
import com.tiankong44.service.impl.AlbumServiceImpl;
import com.tiankong44.util.ConstantUtil;
import com.tiankong44.util.QiniuUpload;
import com.tiankong44.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author zhanghao_SMEICS
 * @Date 2021/4/8 16:38
 * @return
 */

@RestController
@RequestMapping("/admin")
public class AlbumController {
    @Autowired
    private AlbumServiceImpl albumServiceImpl;

    @Autowired
    private RedisUtil redisUtil;

    @PostMapping("/album/queryAlbumList")
    public BaseRes queryAlbumList(@RequestBody String msg, HttpServletRequest request) {
        BaseRes res = new BaseRes();
        User user = (User) request.getSession().getAttribute(User.SESSION_KEY);
        if (user == null) {
            res.setCode(ConstantUtil.RESULT_FAILED);
            res.setDesc("请先登录！");
            return res;
        }
        res = albumServiceImpl.queryAlbumList(msg, user);
        return res;
    }

    @PostMapping("/album/add")
    public BaseRes albumAdd(@RequestBody String msg, HttpServletRequest request) {
        BaseRes res = new BaseRes();
        User user = (User) request.getSession().getAttribute(User.SESSION_KEY);
        if (user == null) {
            res.setCode(ConstantUtil.RESULT_FAILED);
            res.setDesc("请先登录！");
            return res;
        }
        res = albumServiceImpl.addAlbum(msg, user);
        return res;
    }

    @PostMapping("/album/delete")
    public BaseRes albumDelete(@RequestBody String msg, HttpServletRequest request) {
        BaseRes res = new BaseRes();
        User user = (User) request.getSession().getAttribute(User.SESSION_KEY);
        if (user == null) {
            res.setCode(ConstantUtil.RESULT_FAILED);
            res.setDesc("请先登录！");
            return res;
        }
        res = albumServiceImpl.deleteAlbum(msg, user);
        return res;
    }

    @PostMapping("/album/mainPictureUpload")
    @ResponseBody
    public BaseRes mainPictureUpload(@Param("file") MultipartFile file,
                                     HttpServletRequest request, @RequestParam Map<String, String> map) throws Exception {
        BaseRes res = new BaseRes();
        User user = (User) request.getSession().getAttribute(User.SESSION_KEY);
        if (user == null) {
            res.setCode(ConstantUtil.RESULT_FAILED);
            res.setDesc("请先登录！");
            return res;
        }


        String path = "";

        Long userId = user.getId();

        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        path = QiniuUpload.updateFile(file, uuid);
        Gallery gallery = new Gallery();
        gallery.setImgName(uuid);
        gallery.setPath(path);
        gallery.setUploadDate(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        gallery.setUserId(userId);
        gallery.setType(ConstantUtil.ALBUM_PICTURE);
        res = albumServiceImpl.saveMainPicture(gallery, user);

        gallery = (Gallery) res.getData();
        redisUtil.setEx(userId + "album", gallery.getId().toString(), 10, TimeUnit.MINUTES);
        return res;
    }


    @PostMapping("/album/albumPhotosUpload")
    @ResponseBody
    public BaseRes albumPhotosUpload(@Param("files") MultipartFile[] files,
                                     HttpServletRequest request, @RequestParam("albumId") Long albumId) throws Exception {
        BaseRes res = new BaseRes();
        User user = (User) request.getSession().getAttribute(User.SESSION_KEY);
        if (user == null) {
            res.setCode(ConstantUtil.RESULT_FAILED);
            res.setDesc("请先登录！");
            return res;
        }

//        Long albumId = Long.parseLong(request.getParameter("albumId"));
        String path = "";
        Long userId = user.getId();
        List<Gallery> galleryList = new ArrayList<>();
        for (MultipartFile file : files) {
            String name = file.getOriginalFilename();
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            path = QiniuUpload.updateFile(file, uuid);
            Gallery gallery = new Gallery();
            gallery.setImgName(name);
            gallery.setUuid(uuid);
            gallery.setPath(path);
            gallery.setUploadDate(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            gallery.setUserId(userId);
            gallery.setType(ConstantUtil.ALBUM_PICTURE);
            gallery.setAlbumId(albumId);
            galleryList.add(gallery);
        }
        if (galleryList.size() > 0) {
            res = albumServiceImpl.savePictures(galleryList);
        } else {
            res.setCode(ConstantUtil.RESULT_FAILED);
            res.setDesc("图片上传失败！");
        }
        return res;
    }

    /**
     * 相册详情带图片  按日期分类
     *
     * @param request
     * @param msg
     * @return
     * @author zhanghao_SMEICS
     * @Date 2021/4/12 11:25
     */

    @PostMapping("/album/albumDetail")
    public BaseRes albumDetail(@RequestBody String msg, HttpServletRequest request) {
        BaseRes res = new BaseRes();
        User user = (User) request.getSession().getAttribute(User.SESSION_KEY);
        if (user == null) {
            res.setCode(ConstantUtil.RESULT_FAILED);
            res.setDesc("请先登录！");
            return res;
        }
        res = albumServiceImpl.albumDetail(msg, user);
        return res;
    }

    /**
     * 仅相册详情 不带图片
     *
     * @param request
     * @param msg
     * @return
     * @author zhanghao_SMEICS
     * @Date 2021/4/12 11:26
     */

    @PostMapping("/album/justAlbumDetail")
    public BaseRes justAlbum(@RequestBody String msg, HttpServletRequest request) {
        BaseRes res = new BaseRes();
        User user = (User) request.getSession().getAttribute(User.SESSION_KEY);
        if (user == null) {
            res.setCode(ConstantUtil.RESULT_FAILED);
            res.setDesc("请先登录！");
            return res;
        }
        res = albumServiceImpl.justAlbumDetail(msg, user);
        return res;
    }

    /**
     * 搜索相册详情
     *
     * @param request
     * @param msg
     * @return
     * @author zhanghao_SMEICS
     * @Date 2021/4/9 16:57
     */

    @PostMapping("/album/queryAlbumDetail")
    public BaseRes queryAlbumDetail(@RequestBody String msg, HttpServletRequest request) {
        BaseRes res = new BaseRes();
        User user = (User) request.getSession().getAttribute(User.SESSION_KEY);
        if (user == null) {
            res.setCode(ConstantUtil.RESULT_FAILED);
            res.setDesc("请先登录！");
            return res;
        }
        res = albumServiceImpl.queryAlbumDetail(msg, user);
        return res;
    }

    /**
     * 修改相册信息
     *
     * @param request
     * @param msg
     * @return
     * @author zhanghao_SMEICS
     * @Date 2021/4/14 14:08
     */

    @PostMapping("/album/editAlbum")
    public BaseRes editAlbum(@RequestBody String msg, HttpServletRequest request) {
        BaseRes res = new BaseRes();
        User user = (User) request.getSession().getAttribute(User.SESSION_KEY);
        if (user == null) {
            res.setCode(ConstantUtil.RESULT_FAILED);
            res.setDesc("请先登录！");
            return res;
        }
        res = albumServiceImpl.queryAlbumDetail(msg, user);
        return res;
    }

}
