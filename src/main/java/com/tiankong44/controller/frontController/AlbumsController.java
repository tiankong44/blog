package com.tiankong44.controller.frontController;

import com.tiankong44.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/album")
public class AlbumsController {

    @Autowired
    AlbumService albumService;

//    @RequestMapping("/getaAlbums")
//    public BaseRes tag(@RequestBody String msg) {
//        BaseRes res = new BaseRes();
//        res = albumService.listAlbum(msg);
//        return res;
//    }
}
