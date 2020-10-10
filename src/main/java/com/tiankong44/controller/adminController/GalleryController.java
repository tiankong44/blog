package com.tiankong44.controller.adminController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiniu.common.QiniuException;
import com.tiankong44.model.Gallery;
import com.tiankong44.service.impl.GalleryServiceImpl;
import com.tiankong44.util.DateUtils;
import com.tiankong44.util.QiniuUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class GalleryController {
    @Autowired
    private GalleryServiceImpl GalleryServiceImpl;

    //进入图床列表页面
    @GetMapping("/gallery")
    public String Gallery(Model model,
                          @RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum, HttpSession session) {
        Long id = (Long) session.getAttribute("user_id");
        PageHelper.startPage(pageNum, 10);
        List<Gallery> galleryList = GalleryServiceImpl.listImg(id);

        PageInfo<Gallery> pageInfo = new PageInfo<>(galleryList);
        //  List<Gallery> galleryList = GalleryServiceImpl.listImg(id);

        //model.addAttribute("galleryList", galleryList);
        model.addAttribute("pageInfo", pageInfo);
        return "/admin/gallery";
    }

    //搜索图片
    @RequestMapping("/gallery/search")
    public String searchImg(@Param("date") String date, Model model, HttpSession session,
                            @RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum) throws ParseException {
        Long id = (Long) session.getAttribute("user_id");

        if (date == "") {
            PageHelper.startPage(pageNum, 10);
            List<Gallery> galleryList = GalleryServiceImpl.listImg(id);

            PageInfo<Gallery> pageInfo = new PageInfo<>(galleryList);

            model.addAttribute("pageInfo", pageInfo);
            return "/admin/gallery-result";
        } else {

            String[] str = DateUtils.SplitDate(date);
            Date oldDate = DateUtils.StringToDate(str[0]);
            Date newDate = DateUtils.StringToDate(str[1]);
            PageHelper.startPage(pageNum, 10);
            List<Gallery> galleryList = GalleryServiceImpl.findImgByDate(oldDate, newDate, id);
            PageInfo<Gallery> pageInfo = new PageInfo<>(galleryList);
            if (pageInfo.getPages() > 0) {
                model.addAttribute("date", date);
                model.addAttribute("pageInfo", pageInfo);
                model.addAttribute("message", "查询成功");
                return "/admin/gallery-result";
            } else {
                model.addAttribute("date", date);
                model.addAttribute("pageInfo", pageInfo);
                model.addAttribute("message", "此时间段没有上传图片！");
                return "/admin/gallery-result";
            }


        }
    }

    //进入添加图片页面
    @RequestMapping("/gallery-input")
    public String GalleryInput() {

        return "/admin/gallery-input";
    }

    //删除图片
    @GetMapping("/gallery/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) throws QiniuException {
        /*删除七牛云的图片*/
        QiniuUpload.delete(GalleryServiceImpl.findImgById(id).getImgName());
        GalleryServiceImpl.deleteImg(id);
        attributes.addFlashAttribute("message", "删除成功");

        return "redirect:/admin/gallery";
    }


}