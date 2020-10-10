package com.tiankong44.service;

import com.tiankong44.base.entity.BaseRes;
import com.tiankong44.model.Blog;
import com.tiankong44.model.User;

import java.util.List;

public interface BlogService {
    Blog getBlogById(Long id);

    Blog getBlogAndConvert(Long id);

    List<Blog> getBlogByUserId(Long user_id);

    List<Blog> getAllBlog();

    BaseRes getFirstPageBlog( String msg);

    Long getUserIdByBlogId(Long id);

    User getUser(Long user_id);

    void saveBlog(Blog blog);

    Long getMaxBlogId();

    void deleteBlog(Long id);

    void updateBlog(Blog blog);

    void updateBlogViews(Long id);

    void updateBlogPraise(Long id);

    BaseRes  getAllRecommendBlog();

    BaseRes getTopFiveViewBlog();

    BaseRes getFiveNewBlog();

    BaseRes getFiveNewCommentBlog();

    List<Blog> getArchivingBlog();

    BaseRes getFirstPageSearch(String msg);

    List<Blog> getByCondition(String title, List<Long> ids, boolean recommend);

    List<Long> getBlogIdByTagId(Long tag_id);
}
