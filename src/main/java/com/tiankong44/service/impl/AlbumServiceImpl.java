package com.tiankong44.service.impl;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tiankong44.base.entity.BaseRes;
import com.tiankong44.dao.AlbumMapper;
import com.tiankong44.dao.GalleryMapper;
import com.tiankong44.model.Album;
import com.tiankong44.model.Gallery;
import com.tiankong44.model.User;
import com.tiankong44.model.vo.AlbumVo;
import com.tiankong44.model.vo.PhotoVo;
import com.tiankong44.service.AlbumService;
import com.tiankong44.util.ConstantUtil;
import com.tiankong44.util.JsonUtils;
import com.tiankong44.util.RedisUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author zhanghao_SMEICS
 * @Date 2021/4/8 16:38
 * @return
 */

@Service
public class AlbumServiceImpl implements AlbumService {
    @Autowired
    private AlbumMapper albumMapper;
    @Autowired
    private GalleryMapper galleryMapper;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 查询相册列表
     *
     * @param user
     * @param msg
     * @return
     * @author zhanghao_SMEICS
     * @Date 2021/4/8 16:38
     */

    @Override
    public BaseRes queryAlbumList(String msg, User user) {
        BaseRes res = new BaseRes();
        JSONObject reqJson = null;
        try {
            reqJson = JSONObject.fromObject(msg);
            Map<?, ?> checkMap = JsonUtils.noNulls(reqJson, "pageNum", "pageSize");
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
        reqJson.put("userId", user.getId());
        PageHelper.startPage(reqJson.getInt("pageNum"), reqJson.getInt("pageSize"), true);
        List<Album> albumList = albumMapper.queryAlbumList(reqJson);
        PageInfo<Album> pageInfo = new PageInfo<>(albumList);
        res.setCode(ConstantUtil.RESULT_SUCCESS);
        res.setDesc("相册列表查询成功");
        res.setData(pageInfo);
        return res;
    }

    /**
     * 保存相册
     *
     * @param user
     * @param msg
     * @return
     * @author zhanghao_SMEICS
     * @Date 2021/4/9 13:51
     */
    @Override
    @Transactional(timeout = 1000, rollbackFor = Exception.class)
    public BaseRes addAlbum(String msg, User user) {
        BaseRes res = new BaseRes();
        JSONObject reqJson = null;
        try {
            reqJson = JSONObject.fromObject(msg);
            Map<?, ?> checkMap = JsonUtils.noNulls(reqJson, "albumName", "type");
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
        Album album = new Album();
        album.setAlbumName(reqJson.getString("albumName"));
        album.setType(Integer.parseInt(reqJson.getString("type")));
        album.setCreateTime(DateUtil.now());
        album.setUserId(user.getId());
        if (reqJson.containsKey("remark")) {
            album.setRemark(reqJson.getString("remark"));
        }
        String mainPicture = "";
        if (reqJson.containsKey("mainPicture")) {
            mainPicture = reqJson.getString("mainPicture");
            album.setMainPicture(mainPicture);
        }
        boolean flag = albumMapper.addAlbum(album);
        if (!flag) {
            res.setCode(ConstantUtil.RESULT_FAILED);
            res.setDesc("新建相册失败！");
            return res;
        }
        String key = user.getId() + "album";
        if (redisUtil.hasKey(key)) {
            String galleryId = redisUtil.get(key);
            Gallery gallery = galleryMapper.findImgById(Long.parseLong(galleryId));
            if (gallery.getPath().equals(mainPicture.trim())) {
                //修改图片的相册id
                gallery.setAlbumId(album.getId());
                flag = galleryMapper.updateAlbumId(gallery);
                if (!flag) {
                    res.setCode(ConstantUtil.RESULT_FAILED);
                    res.setDesc("新建相册失败！");
                    return res;
                }
                redisUtil.delete(key);
            }

        }

        res.setDesc("新建相册成功！");
        res.setCode(ConstantUtil.RESULT_SUCCESS);

        return res;
    }

    /**
     * 删除相册
     *
     * @param user
     * @param msg
     * @return
     * @author zhanghao_SMEICS
     * @Date 2021/4/9 16:57
     */


    @Override
    @Transactional(timeout = 1000, rollbackFor = Exception.class)
    public BaseRes deleteAlbum(String msg, User user) {
        BaseRes res = new BaseRes();
        JSONObject reqJson = null;
        try {
            reqJson = JSONObject.fromObject(msg);
            Map<?, ?> checkMap = JsonUtils.noNulls(reqJson, "albumId");
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
        Long AlbumId = Long.parseLong(reqJson.getString("albumId"));

        //记录日志
        reqJson.put("userId", user.getId());
        albumMapper.backupAlbum(reqJson);
        galleryMapper.backupAlbumPhotos(reqJson);
        //删除相册
        boolean flag = albumMapper.deleteAlbum(reqJson);
        if (!flag) {
            res.setCode(ConstantUtil.RESULT_FAILED);
            res.setDesc("删除相册失败！");
            return res;
        }

        //删除照片
        galleryMapper.deleteAlbumPhotos(reqJson);

        res.setCode(ConstantUtil.RESULT_SUCCESS);
        res.setDesc("删除相册成功，你可以在回收站找回！");

        return res;
    }

    /**
     * 保存主图
     *
     * @param user
     * @param mainPicture
     * @return
     * @author zhanghao_SMEICS
     * @Date 2021/4/9 13:51
     */

    @Override
    @Transactional(timeout = 1000, rollbackFor = Exception.class)
    public BaseRes saveMainPicture(Gallery mainPicture, User user) {
        BaseRes res = new BaseRes();
        boolean flag = galleryMapper.saveImg(mainPicture);
        if (!flag) {
            res.setCode(ConstantUtil.RESULT_FAILED);
            res.setDesc("图片上传失败！");
            return res;
        }
        res.setCode(ConstantUtil.RESULT_SUCCESS);
        res.setDesc("图片上传成功！");
        res.setData(mainPicture);
        return res;
    }

    /**
     * 批量保存相册照片
     *
     * @param galleryList
     * @return
     * @author zhanghao_SMEICS
     * @Date 2021/4/12 14:27
     */

    @Override
    public BaseRes savePictures(List<Gallery> galleryList) {
        BaseRes res = new BaseRes();

        int flag = galleryMapper.savePictures(galleryList);
        if (flag > 0) {
            res.setCode(ConstantUtil.RESULT_SUCCESS);
            res.setDesc("图片上传成功！");

        } else {
            res.setCode(ConstantUtil.RESULT_FAILED);
            res.setDesc("图片上传失败！");
        }
        return res;
    }

    /**
     * 相册详情  相片按照日期分类
     *
     * @param user
     * @param msg
     * @return
     * @author zhanghao_SMEICS
     * @Date 2021/4/9 16:57
     */

    @Override
    public BaseRes albumDetail(String msg, User user) {
        BaseRes res = new BaseRes();
        JSONObject reqJson = null;
        try {
            reqJson = JSONObject.fromObject(msg);
            Map<?, ?> checkMap = JsonUtils.noNulls(reqJson, "albumId");
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
//        Long AlbumId = Long.parseLong(reqJson.getString("albumId"));
        reqJson.put("userId", user.getId());

        AlbumVo album = albumMapper.getAlbumDetail(reqJson);

        List<PhotoVo> galleryList = album.getPhotos();
        Set<String> dateSet = new HashSet();
        for (PhotoVo photoVo : galleryList) {
            String uploadTime = photoVo.getUploadDate();
            dateSet.add(uploadTime);
        }
//        Map<String, List<Gallery>> resAlbum = new HashMap<>();
        List<AlbumVo> resAlbum = new ArrayList<>();
        for (String s : dateSet) {
            List<PhotoVo> list = new ArrayList<>();
            for (PhotoVo photoVo : galleryList) {
                if (s.equals(photoVo.getUploadDate())) {
                    photoVo.setIscheck(false);
                    list.add(photoVo);
                }
            }
            AlbumVo albumParam = new AlbumVo();
            albumParam.setCreateTime(s);
            albumParam.setPhotos(list);
            resAlbum.add(albumParam);
        }

        Map<String, Object> resMap = new HashMap<>();
        resMap.put("albumName", album.getAlbumName());
        resMap.put("data", resAlbum);
        res.setDesc("相册查询成功！");
        res.setCode(ConstantUtil.RESULT_SUCCESS);
        res.setData(resMap);
        return res;
    }


    /**
     * 搜索相册详情
     *
     * @param user
     * @param msg
     * @return
     * @author zhanghao_SMEICS
     * @Date 2021/4/9 16:57
     */

    @Override
    public BaseRes queryAlbumDetail(String msg, User user) {
        BaseRes res = new BaseRes();
        JSONObject reqJson = null;
        try {
            reqJson = JSONObject.fromObject(msg);
            Map<?, ?> checkMap = JsonUtils.noNulls(reqJson, "albumId");
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
//        Long AlbumId = Long.parseLong(reqJson.getString("albumId"));
        reqJson.put("userId", user.getId());

        AlbumVo album = albumMapper.queryAlbumDetail(reqJson);

        List<PhotoVo> galleryList = album.getPhotos();
        Set<String> dateSet = new HashSet();
        for (PhotoVo photoVo : galleryList) {
            String uploadTime = photoVo.getUploadDate();
            dateSet.add(uploadTime);
        }
//        Map<String, List<Gallery>> resAlbum = new HashMap<>();
        List<AlbumVo> resAlbum = new ArrayList<>();
        for (String s : dateSet) {
            List<PhotoVo> list = new ArrayList<>();
            for (PhotoVo photoVo : galleryList) {
                if (s.equals(photoVo.getUploadDate())) {
                    photoVo.setIscheck(false);
                    list.add(photoVo);
                }
            }
            AlbumVo albumParam = new AlbumVo();
            albumParam.setCreateTime(s);
            albumParam.setPhotos(list);
            resAlbum.add(albumParam);
        }

        Map<String, Object> resMap = new HashMap<>();
        resMap.put("albumName", album.getAlbumName());
        resMap.put("remark", album.getRemark());
        resMap.put("data", resAlbum);
        res.setDesc("相册查询成功！");
        res.setCode(ConstantUtil.RESULT_SUCCESS);
        res.setData(resMap);
        return res;
    }

    /**
     * 仅相册详情 不带图片
     *
     * @param user
     * @param msg
     * @return
     * @author zhanghao_SMEICS
     * @Date 2021/4/12 11:27
     */

    @Override
    public BaseRes justAlbumDetail(String msg, User user) {
        BaseRes res = new BaseRes();
        JSONObject reqJson = null;
        try {
            reqJson = JSONObject.fromObject(msg);
            Map<?, ?> checkMap = JsonUtils.noNulls(reqJson, "albumId");
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

        reqJson.put("userId", user.getId());
        AlbumVo album = albumMapper.getJustAlbumDetail(reqJson);
        if (album != null) {
            res.setCode(ConstantUtil.RESULT_SUCCESS);
            res.setData(album);
            res.setDesc("相册详情查询成功！");
        } else {
            res.setCode(ConstantUtil.RESULT_FAILED);
            res.setData(album);
            res.setDesc("相册详情查询异常！");
        }
        return res;
    }
}
