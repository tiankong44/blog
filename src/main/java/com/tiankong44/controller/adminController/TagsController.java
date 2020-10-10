package com.tiankong44.controller.adminController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiniu.common.QiniuException;
import com.tiankong44.model.Tag;
import com.tiankong44.service.impl.TagServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class TagsController {
    @Autowired
    private TagServiceImpl tagService;

    @GetMapping("/tags")
    public String tags(Model model,
                       @RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum) {
        PageHelper.startPage(pageNum, 10);
        List<Tag> tagList = tagService.getAdminTag();
        PageInfo<Tag> pageInfo = new PageInfo<>(tagList);

        model.addAttribute("pageInfo", pageInfo);
        return "/admin/tags";
    }

    //进入添加标签页面
    @GetMapping("/tags-input")
    public String tagInput() {

        return "/admin/tags-input";
    }

    @PostMapping("/tags-input")
    public String tagSave(@Param("name") String name, RedirectAttributes attributes) {
        Tag tag = new Tag();
        tag.setName(name);
        Tag tag1 = tagService.getByName(tag.getName());
        if (tag1 == null) {
            tagService.saveTag(tag);
        } else {
            attributes.addAttribute("message", "标签已存在！");
        }
        return "redirect:/admin/tags";
    }

    //删除标签
    @GetMapping("/tags/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
      try {
          tagService.deleteTag(id);
          attributes.addAttribute("message", "删除成功！");
      }catch (Exception e){
          attributes.addAttribute("message", "删除失败，该标签下有相关博客！");
      }
        return "redirect:/admin/tags";
    }

    @GetMapping("/tags/{id}/input")
    public String update(Model model, @PathVariable Long id, RedirectAttributes attributes, @Param("name") String name) {
        model.addAttribute("tag", tagService.getById(id));
        return "/admin/tags-update";
    }

    @PostMapping("/tags/update")
    public String update(RedirectAttributes attributes, @Param("id") Long id, @Param("name") String name) {
        Tag tag = new Tag();
        tag.setId(id);
        tag.setName(name);
        Tag tag1 = tagService.getByName(name);
        if (tag1 == null) {
            tagService.updateTag(tag);
        } else {
            attributes.addAttribute("message", "标签已存在！");
        }
        return "redirect:/admin/tags";
    }

    @RequestMapping("/tags/search")
    public String search(Model model, @RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum, @Param("tagName") String tagName) throws QiniuException {
        if (tagName == "") {
            return "redirect:/admin/tags";
        }

        PageHelper.startPage(pageNum, 10);
//        Tag tag = tagService.getByName(tagName);
//        List<Tag> tagList = new ArrayList<>();
//        tagList.add(tag);
        List<Tag> tagList = tagService.getSearch(tagName);
        PageInfo<Tag> pageInfo = new PageInfo<>(tagList);

        model.addAttribute("pageInfo", pageInfo);

        return "/admin/tags";
    }
}