package com.tiankong44.service;

import com.tiankong44.model.Gallery;

import java.util.Date;
import java.util.List;

public interface GalleryService {
    void saveImg(Gallery Gallery);

    void deleteImg(Long id);

    List<Gallery> listImg(Long id);

    void updateImg(Long id);

    Gallery findImgById(Long id);

    List<Gallery> findImgByDate(Date oldDate, Date newDate, Long id);

}
