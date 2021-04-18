package com.tiankong44.service;

import com.tiankong44.base.entity.BaseRes;
import com.tiankong44.model.Gallery;
import com.tiankong44.model.User;

import java.util.Date;
import java.util.List;

public interface GalleryService {
    boolean saveImg(Gallery Gallery);

    void deleteImg(Long id);

    List<Gallery> listImg(Long id);

    void updateImg(Long id);

    Gallery findImgById(Long id);

    List<Gallery> findImgByDate(Date oldDate, Date newDate, Long id);

    /**
     * 删除相册中的图片
     *
     * @param user
     * @param msg
     * @return
     * @author zhanghao_SMEICS
     * @Date 2021/4/12 10:16
     */

    BaseRes deleteAlbumGallery(String msg, User user);
}
