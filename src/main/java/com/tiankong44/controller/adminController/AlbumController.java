package com.tiankong44.controller.adminController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiniu.common.QiniuException;
import com.tiankong44.model.Album;
import com.tiankong44.service.impl.AlbumServiceImpl;
import com.tiankong44.util.DateUtils;
import com.tiankong44.util.QiniuUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * @ClassName AlbumController
 * @Description TODO
 * @Author 12481
 * @Date 21:13
 * @Version 1.0
 **/
@Controller
@RequestMapping("/admin")
public class AlbumController {
    @Autowired
    private AlbumServiceImpl AlbumServiceImpl;

    @GetMapping("/album")
    public String Gallery(Model model,
                          @RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum, HttpSession session) {
        Long id = (Long) session.getAttribute("user_id");
        PageHelper.startPage(pageNum, 10);
        List<Album> albumsList = AlbumServiceImpl.listImg(id);
        PageInfo<Album> pageInfo = new PageInfo<>(albumsList);
        model.addAttribute("pageInfo", pageInfo);
        return "/admin/album";
    }


    //进入添加图片页面
    @RequestMapping("/album-input")
    public String GalleryInput() {

        return "/admin/album-input";
    }

    //搜索图片
    @RequestMapping("/album/search")
    public String searchImg(@Param("date") String date, Model model, HttpSession session,
                            @RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum) throws ParseException {
        Long id = (Long) session.getAttribute("user_id");

        if (date == "") {
            PageHelper.startPage(pageNum, 10);
            List<Album> albumList = AlbumServiceImpl.listImg(id);

            PageInfo<Album> pageInfo = new PageInfo<>(albumList);

            model.addAttribute("pageInfo", pageInfo);
            return "/admin/album-result";
        } else {

            String[] str = DateUtils.SplitDate(date);
            Date oldDate = DateUtils.StringToDate(str[0]);
            Date newDate = DateUtils.StringToDate(str[1]);
            PageHelper.startPage(pageNum, 10);
            List<Album> albumList = AlbumServiceImpl.findImgByDate(oldDate, newDate, id);
            PageInfo<Album> pageInfo = new PageInfo<>(albumList);
            if (pageInfo.getPages() > 0) {
                model.addAttribute("date", date);
                model.addAttribute("pageInfo", pageInfo);
                model.addAttribute("message", "查询成功");
                return "/admin/album-result";
            } else {
                model.addAttribute("date", date);
                model.addAttribute("pageInfo", pageInfo);
                model.addAttribute("message", "此时间段没有上传图片！");
                return "/admin/album-result";
            }

        }
    }

    //删除图片
    @GetMapping("/album/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) throws QiniuException {
        /*删除七牛云的图片*/
        QiniuUpload.delete(AlbumServiceImpl.findImgById(id).getImgName());
        AlbumServiceImpl.deleteImg(id);
        attributes.addFlashAttribute("message", "删除成功");

        return "redirect:/admin/album";
    }


}
