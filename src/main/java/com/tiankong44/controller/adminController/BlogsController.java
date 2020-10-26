//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.tiankong44.controller.adminController;

import com.qiniu.common.QiniuException;
import com.tiankong44.model.Blog;
import com.tiankong44.model.Tag;
import com.tiankong44.service.impl.BlogServiceImpl;
import com.tiankong44.service.impl.TagServiceImpl;
import com.tiankong44.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping({"/admin"})
public class BlogsController {
    @Autowired
    private BlogServiceImpl blogService;
    @Autowired
    private TagServiceImpl tagService;

    public BlogsController() {
    }

//    @RequestMapping({"/blogs"})
//    public String blogs(Model model, @RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum, HttpSession session) {
//        Long id = (Long) session.getAttribute("user_id");
//        PageHelper.startPage(pageNum, 10);
//        List<Blog> blogList = this.blogService.getBlogByUserId(id);
//        List<Tag> selectTag = this.tagService.getAdminTag();
//        Iterator var7 = blogList.iterator();
//
//        while (var7.hasNext()) {
//            Blog blog = (Blog) var7.next();
//            List<Tag> tagList = this.tagService.getTagsByBlogId(blog.getId());
//            String showTags = " ";
//
//            Tag tag;
//            for (Iterator var11 = tagList.iterator(); var11.hasNext(); showTags = showTags + tag.getName() + "  ") {
//                tag = (Tag) var11.next();
//            }
//
//            blog.setTags(tagList);
//            blog.setShowTags(showTags);
//        }
//
//        PageInfo<Blog> pageInfo = new PageInfo(blogList);
//        model.addAttribute("pageInfo", pageInfo);
//        model.addAttribute("selectTag", selectTag);
//        return "admin/blogs";
//    }

//    @RequestMapping({"/blogs/blogs-input"})
//    public String blogsInput(Model model) {
//        List<Tag> selectTagId = this.tagService.getAdminTag();
//        model.addAttribute("selectTagId", selectTagId);
//        return "admin/blogs-input";
//    }

//    @RequestMapping({"/blogs/save"})
//    public String blogsSave(Model model, @Param("title") String title, @Param("firstPic") String firstPic, @Param("content") String content, @Param("desc") String desc, @Param("selectTagId") Long selectTagId, @Param("appreciation") String appreciation, @Param("recommend") String recommend, @Param("commentabled") String commentabled, @Param("published") String published, HttpSession session) {
//        List<Tag> selectTag = this.tagService.getAdminTag();
//        model.addAttribute("selectTag", selectTag);
//        Long id = (Long) session.getAttribute("user_id");
//        Blog blog = new Blog();
//        Date date = new Date();
//        blog.setCreateTime(DateUtils.timeToString(date));
//        blog.setUpdateTime(DateUtils.timeToString(date));
//        blog.setUserId(id);
//        blog.setTitle(title);
//        blog.setDescription(desc);
//        blog.setFirstPicture(firstPic);
//        blog.setContent(content);
//        List<Tag> tagList = new ArrayList();
//        tagList.add(this.tagService.getById(selectTagId));
//        blog.setTags(tagList);
////        blog.setTagIds(selectTagId.toString());
//        if ("on".equals(appreciation)) {
//            blog.setAppreciation(true);
//        } else {
//            blog.setAppreciation(false);
//        }
//
//        if ("on".equals(recommend)) {
//            blog.setRecommend(true);
//        } else {
//            blog.setRecommend(false);
//        }
//
//        if ("on".equals(commentabled)) {
//            blog.setCommentabled(true);
//        } else {
//            blog.setCommentabled(false);
//        }
//
//        if ("on".equals(published)) {
//            blog.setPublished(true);
//        } else {
//            blog.setPublished(false);
//        }
//
//        this.blogService.saveBlog(blog);
//        this.tagService.saveBlogAndTag(this.blogService.getMaxBlogId(), selectTagId);
//        return "redirect:/admin/blogs";
//    }

    @GetMapping({"/blogs/{id}/delete"})
    public String delete(@PathVariable Long id, RedirectAttributes attributes) throws QiniuException {
        this.tagService.deleteBlogAndTag(id);
        this.blogService.deleteBlog(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/blogs";
    }

//    @GetMapping({"/blogs/{id}/update"})
//    public String toUpdate(@PathVariable Long id, Model model) {
//        List<Tag> selectTagId = this.tagService.getAdminTag();
//        model.addAttribute("selectTagId", selectTagId);
//        Blog blog = this.blogService.getBlogById(id);
//        System.out.println(blog.getTags().toString());
//        model.addAttribute("blog", blog);
//        return "admin/blogs-update";
//    }

//    @RequestMapping({"/blogs/search"})
//    public String search(@RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum, @Param("title") String title, @Param("selectTag") Long selectTag, @Param("recommend") String recommend, Model model, HttpSession session) {
//        Long id = (Long) session.getAttribute("user_id");
//        List<Tag> selectTagId = this.tagService.getAdminTag();
//        model.addAttribute("selectTagId", selectTagId);
//        List<Long> ids = null;
//        if (selectTag == 0L) {
//            ids = null;
//        } else {
//            ids = this.blogService.getBlogIdByTagId(selectTag);
//            if (ids.size() == 0) {
//                ids = null;
//                model.addAttribute("message", "暂无关于此标签的博客，给你推荐其他相关信息的结果");
//            }
//        }
//
//        boolean re = false;
//        if ("on".equals(recommend)) {
//            re = true;
//        } else {
//            re = false;
//        }
//
//        PageHelper.startPage(pageNum, 10);
//        List<Blog> blogList = this.blogService.getByCondition(title, ids, re);
//        Iterator var12 = blogList.iterator();
//
//        while (var12.hasNext()) {
//            Blog blog = (Blog) var12.next();
//            List<Tag> tagList = this.tagService.getTagsByBlogId(blog.getId());
//            String showTags = " ";
//
//            Tag tag;
//            for (Iterator var16 = tagList.iterator(); var16.hasNext(); showTags = showTags + tag.getName() + "  ") {
//                tag = (Tag) var16.next();
//            }
//
//            blog.setTags(tagList);
//            blog.setShowTags(showTags);
//        }
//
//        PageInfo<Blog> pageInfo = new PageInfo(blogList);
//        if (pageInfo.getTotal() == 0L) {
//            model.addAttribute("message", "暂时没有相关博客,给你推荐其他相关信息的结果");
//            PageHelper.startPage(pageNum, 10);
//            List<Blog> blogList1 = this.blogService.getBlogByUserId(id);
//            pageInfo = new PageInfo(blogList1);
//        }
//
//        model.addAttribute("pageInfo", pageInfo);
//        model.addAttribute("title", title);
//        model.addAttribute("selectTag", selectTag);
//        model.addAttribute("recommend", recommend);
//        return "admin/blogs-result";
//    }

    @PostMapping({"/blogs/update"})
    public String update(Model model, @Param("title") String title, @Param("firstPic") String firstPic, @Param("content") String content, @Param("desc") String desc, @Param("selectTagId") Long selectTagId, @Param("appreciation") String appreciation, @Param("recommend") String recommend, @Param("commentabled") String commentabled, @Param("published") String published, @Param("id") Long id) {
        Blog blog = this.blogService.getBlogById(id);
        Date date = new Date();
        blog.setUpdateTime(DateUtils.timeToString(date));
        blog.setTitle(title);
        blog.setFirstPicture(firstPic);
        blog.setContent(content);
        blog.setDescription(desc);
        List<Tag> tagList = new ArrayList();
        tagList.add(this.tagService.getById(selectTagId));
        blog.setTags(tagList);
//        blog.setTagIds(selectTagId.toString());
        if ("on".equals(appreciation)) {
            blog.setAppreciation(true);
        } else {
            blog.setAppreciation(false);
        }

        if ("on".equals(recommend)) {
            blog.setRecommend(true);
        } else {
            blog.setRecommend(false);
        }

        if ("on".equals(commentabled)) {
            blog.setCommentabled(true);
        } else {
            blog.setCommentabled(false);
        }

        if ("on".equals(published)) {
            blog.setPublished(true);
        } else {
            blog.setPublished(false);
        }

        this.blogService.updateBlog(blog);
        this.tagService.updateBlogAndTag(id, selectTagId);
        return "redirect:/admin/blogs";
    }
}
