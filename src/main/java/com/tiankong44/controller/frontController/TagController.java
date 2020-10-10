package com.tiankong44.controller.frontController;

import com.tiankong44.service.BlogService;
import com.tiankong44.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class TagController {
    @Autowired
    private TagService tagService;
    @Autowired
    private BlogService blogService;

//    @GetMapping("/tag")
//    public String tag(Model model, @RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum) {
//        List<Tag> tagList = tagService.getAdminTag();
//        PageHelper.startPage(pageNum, 8);
//        List<Blog> blogList = blogService.getFirstPageBlog();
//        PageInfo<Blog> pageInfo = new PageInfo<>(blogList);
//        model.addAttribute("pageInfo", pageInfo);
//        model.addAttribute("tagList", tagList);
//
//        return "tag";
//    }

//    @GetMapping("/tag/{id}")
//    public String postData(@PathVariable Long id, Model model, @RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum, HttpSession session) {
//        session.setAttribute("tagId", id);
//
//        PageHelper.startPage(1, 8);
//        List<Blog> blogList = tagService.getByTagId(id);
//        PageInfo<Blog> pageInfo = new PageInfo<>(blogList);
//        pageInfo.setPages(1);
//        // System.out.println(pageInfo.getPages());
//        //System.out.println(pageInfo.getTotal());
//        model.addAttribute("pageInfo", pageInfo);
//        List<Tag> tagList = tagService.getAdminTag();
//        //  model.addAttribute("tagList", tagList);
//
//        return "tag :: blogList1";
//    }

//    @PostMapping("/tag")
//    @ResponseBody
//    public BaseRes getBlogListByTagId(@RequestBody String msg) {
//        BaseRes res = new BaseRes();
//        res = tagService.getByTagId(msg);
//        return res;
//    }

//    @GetMapping("/tag/{id}/search")
//    public String search(@PathVariable Long id, Model model, @RequestParam(defaultValue = "2", value = "pageNum") Integer pageNum, HttpSession session) {
//        session.setAttribute("tagId", id);
//
//        PageHelper.startPage(pageNum, 8);
//        PageInfo<Blog> pageInfo = new PageInfo<>(tagService.getByTagId(id));
//        // System.out.println(pageInfo.getPages());
//        //System.out.println(pageInfo.getTotal());
//        model.addAttribute("pageInfo", pageInfo);
//        List<Tag> tagList = tagService.getAdminTag();
//        model.addAttribute("tagList", tagList);
//
//        return "tag-result";
//    }

}
