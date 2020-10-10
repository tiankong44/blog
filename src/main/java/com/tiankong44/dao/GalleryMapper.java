package com.tiankong44.dao;

import com.tiankong44.model.Gallery;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@Mapper
public interface GalleryMapper {

    void saveImg(Gallery Gallery);

    List<Gallery> listImg(Long id);

    void deleteImg(Long id);

    void updateImg(Long id);

    Gallery findImgById(Long id);

    List<Gallery> findImgByDate(Date oldDate, Date newDate, Long id);
}
