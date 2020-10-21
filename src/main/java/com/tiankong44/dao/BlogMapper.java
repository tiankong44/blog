package com.tiankong44.dao;

import com.tiankong44.model.Blog;
import com.tiankong44.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface BlogMapper {
    Map getBlogByBlogId(Long id);

    Blog getBlogById(Long id);

    List<Blog> getBlogByUserId(Long user_id);

    List<Blog> getAllBlog();

    List<Map> getFirstPageBlog();

    Long getMaxBlogId();

    Long getUserIdByBlogId(Long id);

    void saveBlog(Blog blog);

    User getUser(Long user_id);

    void deleteBlog(Long id);

    void updateBlog(Blog blog);

    void updateBlogViews(Long id);

    void updateBlogPraise(Long id);

    List<Long> getBlogIdByTagId(Long tag_id);

    List<Blog> getTopFiveViewBlog();

    List<Blog> getFiveNewBlog();

    List<Blog> getFiveNewCommentBlog(Long blogId);

    List<Blog> getArchivingBlog();

    List<Blog> getFirstPageSearch(String title);

    List<Blog> getByCondition(String title, List<Long> ids, boolean recommend);

    List<Blog> getBlogListByTagId(Long tagId);
}
