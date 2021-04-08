package com.tiankong44.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tiankong44.base.entity.BaseRes;
import com.tiankong44.dao.AlbumMapper;
import com.tiankong44.model.Album;
import com.tiankong44.service.AlbumService;
import com.tiankong44.util.ConstantUtil;
import com.tiankong44.util.JsonUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName AlbumServiceImpl
 * @Description TODO
 * @Author 12481
 * @Date 21:24
 * @Version 1.0
 **/
@Service
public class AlbumServiceImpl implements AlbumService {
    @Autowired
    private AlbumMapper albumMapper;

    @Override
    public void saveImg(Album album) {
        albumMapper.saveImg(album);
    }

    @Override
    public void deleteImg(Long id) {
        albumMapper.deleteImg(id);
    }

    @Override
    public List<Album> listImg(Long id) {
        return albumMapper.listImg(id);
    }

    @Override
    public void updateImg(Long id) {

    }

    @Override
    public Album findImgById(Long id) {
        return albumMapper.findImgById(id);
    }

    public AlbumServiceImpl() {
    }

    @Override
    public BaseRes listAlbum(String msg) {

        BaseRes res = new BaseRes();
        JSONObject reqJson = null;
        int pageNum;
        int pageSize;
        try {
            reqJson = JSONObject.fromObject(msg);
            Map<?, ?> checkMap = JsonUtils.noNulls(reqJson, "pageNum", "pageSize");
            if (checkMap != null) {
                res.setCode(1);
                res.setDesc("请求参数错误");
                return res;
            }
            pageNum = reqJson.getInt("pageNum");
            pageSize = reqJson.getInt("pageSize");
            PageHelper.startPage(pageNum, pageSize);
            List<Album> dateList = albumMapper.getAllDate();
            PageInfo<Album> pageInfo = new PageInfo(dateList);
            for (Album day : dateList) {
                List<Album> dayList = albumMapper.findImgByGroupDate(day.getUploadDate());
                day.setAlbumList(dayList);
            }
            res.setCode(ConstantUtil.RESULT_SUCCESS);
            res.setDesc("相册查询成功");
            res.setData(pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(1);
            res.setDesc("请求参数异常");
            return res;
        }


        return res;
    }

    @Override
    public List<Album> findImgByDate(Date oldDate, Date newDate, Long id) {
        return albumMapper.findImgByDate(oldDate, newDate, id);
    }
}
