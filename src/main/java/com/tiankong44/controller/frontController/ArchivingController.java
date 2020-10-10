package com.tiankong44.controller.frontController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tiankong44.model.Blog;
import com.tiankong44.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping
public class ArchivingController {
    @Autowired
    BlogService blogService;

    @RequestMapping("/archiving")
    public String tag(Model model, @RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum) {
        PageHelper.startPage(pageNum, 10);
        List<Blog> blogList = blogService.getArchivingBlog();
        PageInfo<Blog> pageInfo = new PageInfo<>(blogList);

        model.addAttribute("pageInfo", pageInfo);
        return "archiving";
    }

}
