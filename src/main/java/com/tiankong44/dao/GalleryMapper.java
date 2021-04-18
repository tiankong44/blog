package com.tiankong44.dao;

import com.tiankong44.model.Gallery;
import net.sf.json.JSONObject;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@Mapper
public interface GalleryMapper {

    boolean saveImg(Gallery Gallery);

    List<Gallery> listImg(Long id);

    void deleteImg(Long id);


    Gallery findImgById(Long id);

    List<Gallery> findImgByDate(Date oldDate, Date newDate, Long id);

    boolean updateAlbumId(Gallery Gallery);


    /**
     * 备份相册所有图片
     *
     * @param reqJson
     * @return
     * @author zhanghao_SMEICS
     * @Date 2021/4/9 15:07
     */

    void backupAlbumPhotos(JSONObject reqJson);

    /**
     * 备份相册指定图片
     *
     * @param reqJson
     * @return
     * @author zhanghao_SMEICS
     * @Date 2021/4/9 15:07
     */

    void backupAlbumPhotosChecked(JSONObject reqJson);

    /**
     * 删除相册所有照片
     *
     * @param reqJson
     * @return
     * @author zhanghao_SMEICS
     * @Date 2021/4/12 10:26
     */

    void deleteAlbumPhotos(JSONObject reqJson);

    /**
     * 批量删除相册指定照片
     *
     * @param reqJson
     * @return
     * @author zhanghao_SMEICS
     * @Date 2021/4/12 10:26
     */

    void deleteAlbumPhotosChecked(JSONObject reqJson);

    /**
     * 批量保存相册指定照片
     *
     * @param list

     * @return
     * @author zhanghao_SMEICS
     * @Date 2021/4/12 10:26
     */
    int savePictures(List<Gallery> list);
}
