package com.tiankong44.service;

import com.tiankong44.base.entity.BaseRes;
import com.tiankong44.model.Gallery;
import com.tiankong44.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AlbumService {


    BaseRes queryAlbumList(String msg, User user);

    BaseRes saveMainPicture(Gallery mainPicture, User user);

    BaseRes addAlbum(String msg, User user);

    BaseRes deleteAlbum(String msg, User user);

    BaseRes albumDetail(String msg, User user);
    BaseRes queryAlbumDetail(String msg, User user);

    BaseRes justAlbumDetail(String msg, User user);

    BaseRes savePictures(List<Gallery> galleryList);
}
