package com.tiankong44.service;

import com.tiankong44.base.entity.BaseRes;
import com.tiankong44.model.Album;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface AlbumService {
    void saveImg(Album album);

    void deleteImg(Long id);

    List<Album> listImg(Long id);

    void updateImg(Long id);

    Album findImgById(Long id);

    BaseRes listAlbum(String msg);

    List<Album> findImgByDate(Date oldDate, Date newDate, Long id);

}
