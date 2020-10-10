package com.tiankong44.dao;

import com.tiankong44.model.Blog;
import com.tiankong44.model.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface TagMapper {
    void saveTag(Tag tag);

    void saveBlogAndTag(Long blog_id, Long tag_id);

    int deleteTag(Long id);

    void deleteBlogAndTag(Long blog_id);

    void updateTag(Tag tag);

    void updateBlogAndTag(Long blog_id, Long tag_id);

    Tag getById(Long id);

    List<Tag> getfirstPageTag();

    List<Tag> getSearch(String name);

    Tag getByName(String name);

    List<Tag> getAllTag();

    List<Tag> getTagsByBlogId(Long blog_id);

    List<Tag> getAdminTag();

    List<Blog> getByTagId(Long tagId);
}
