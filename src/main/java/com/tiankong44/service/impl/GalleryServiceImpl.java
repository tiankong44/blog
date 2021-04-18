package com.tiankong44.service.impl;

import com.tiankong44.base.entity.BaseRes;
import com.tiankong44.dao.GalleryMapper;
import com.tiankong44.model.Gallery;
import com.tiankong44.model.User;
import com.tiankong44.service.GalleryService;
import com.tiankong44.util.ConstantUtil;
import com.tiankong44.util.JsonUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class GalleryServiceImpl implements GalleryService {
    @Autowired
    private GalleryMapper galleryMapper;


    @Override
    public List<Gallery> listImg(Long id) {

        return galleryMapper.listImg(id);
    }

    @Override
    public boolean saveImg(Gallery Gallery) {
        boolean flag = galleryMapper.saveImg(Gallery);
        return flag;
    }

    @Override
    public void deleteImg(Long id) {
        galleryMapper.deleteImg(id);
    }

    @Override
    public void updateImg(Long id) {

    }

    @Override
    public Gallery findImgById(Long id) {
        return galleryMapper.findImgById(id);
    }

    @Override
    public List<Gallery> findImgByDate(Date oldDate, Date newDate, Long id) {
        return null;
    }


    /**
     * 删除相册中的图片
     *
     * @param user
     * @param msg
     * @return
     * @author zhanghao_SMEICS
     * @Date 2021/4/12 10:17
     */
    @Override
    @Transactional(timeout = 1000, rollbackFor = Exception.class)
    public BaseRes deleteAlbumGallery(String msg, User user) {
        BaseRes res = new BaseRes();
        JSONObject reqJson = null;
        try {
            reqJson = JSONObject.fromObject(msg);
            Map<?, ?> checkMap = JsonUtils.noNulls(reqJson, "photos","albumId");
            if (checkMap != null) {
                res.setCode(ConstantUtil.RESULT_FAILED);
                res.setDesc("请求参数错误");
                return res;
            }

        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(ConstantUtil.RESULT_FAILED);
            res.setDesc("请求参数错误");
            return res;
        }
        String photos = reqJson.getString("photos");
        String[] photosArr = photos.split(",");
        List<String> photoList = Arrays.asList(photosArr);
        Long userId = user.getId();
        reqJson.put("userId", userId);
        reqJson.put("photoList", photoList);

        //备份照片
        galleryMapper.backupAlbumPhotosChecked(reqJson);
        //删除照片
        galleryMapper.deleteAlbumPhotosChecked(reqJson);

        res.setCode(ConstantUtil.RESULT_SUCCESS);
        res.setDesc("删除图片成功");

        return res;
    }
}
