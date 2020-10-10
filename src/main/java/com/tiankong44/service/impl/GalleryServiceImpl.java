package com.tiankong44.service.impl;

import com.tiankong44.dao.GalleryMapper;
import com.tiankong44.model.Gallery;
import com.tiankong44.service.GalleryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class GalleryServiceImpl implements GalleryService {
    @Autowired
    private GalleryMapper galleryMapper;


    @Override
    public List<Gallery> listImg(Long id) {

        return galleryMapper.listImg(id);
    }

    @Override
    public void saveImg(Gallery Gallery) {
        galleryMapper.saveImg(Gallery);
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
        return galleryMapper.findImgByDate(oldDate, newDate, id);
    }
}
