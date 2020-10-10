package com.tiankong44.service.impl;

import com.tiankong44.dao.AlbumMapper;
import com.tiankong44.model.Album;
import com.tiankong44.service.AlbumService;
import com.tiankong44.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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
    public List<Album> listAlbum() {

        List<Album> dateList = albumMapper.getAllDate();
        for (Album day : dateList) {
            List<Album> dayList = albumMapper.findImgByGroupDate(DateUtils.dateToString(day.getUploadDate(), DateUtils.DEFAULT_DATE_PATTERN));

            day.setAlbumList(dayList);

        }
        return dateList;
    }

    @Override
    public List<Album> findImgByDate(Date oldDate, Date newDate, Long id) {
        return albumMapper.findImgByDate(oldDate, newDate, id);
    }
}
