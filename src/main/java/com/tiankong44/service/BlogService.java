//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.tiankong44.service;

import com.tiankong44.base.entity.BaseRes;
import com.tiankong44.model.Blog;
import com.tiankong44.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public interface BlogService {
    Blog getBlogById(Long id);

    BaseRes getBlogDetail(String msg, HttpServletRequest request);

    BaseRes getBlogAndConvert(HttpSession session, String msg, HttpServletRequest request);

    BaseRes queryBlogList(String msg, HttpServletRequest request);

    List<Blog> getAllBlog();

    BaseRes getFirstPageBlog(String msg);

    Long getUserIdByBlogId(Long id);

    User getUser(Long user_id);

    BaseRes saveBlog(String msg, HttpServletRequest request);



    BaseRes deleteBlog(String msg);


    void updateBlogViews(Long id);

    BaseRes updateBlogPraise(HttpServletRequest request, String msg);

    BaseRes getComment(String msg);

    BaseRes getAllRecommendBlog();

    BaseRes getTopFiveViewBlog();

    BaseRes getFiveNewBlog();

    BaseRes getFiveNewCommentBlog();

    BaseRes getArchivingBlog(String msg);

    BaseRes getFirstPageSearch(String msg);

    BaseRes getrecommend(HttpServletRequest request);

    BaseRes getBlogListByTagId(String msg);

    BaseRes updateBlog(String msg, HttpServletRequest request);
}
