package com.tiankong44.dao;

import com.tiankong44.model.Album;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@Mapper
public interface AlbumMapper {
    void saveImg(Album album);

    void deleteImg(Long id);

    List<Album> listImg(Long id);

    List<Album> listAlbum();
    List<Album> getAllDate();
    void updateImg(Long id);

    Album findImgById(Long id);
    List<Album> findImgByGroupDate(String day);
    List<Album> findImgByDate(Date oldDate, Date newDate, Long id);
}
