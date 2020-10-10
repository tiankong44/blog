package com.tiankong44.controller.frontController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tiankong44.model.Album;
import com.tiankong44.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping
public class AlbumsController {

    @Autowired
    AlbumService albumService;

    @RequestMapping("/albums")
    public String tag(Model model,@RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum) {
        PageHelper.startPage(pageNum, 10);
        List<Album> albumList = albumService.listAlbum();
        PageInfo<Album> pageInfo = new PageInfo<>(albumList);
        model.addAttribute("pageInfo", pageInfo);
       // model.addAttribute("albumList", albumList);
        return "albums";
    }
}
