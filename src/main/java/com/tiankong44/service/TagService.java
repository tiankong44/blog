package com.tiankong44.service;

import com.tiankong44.base.entity.BaseRes;
import com.tiankong44.model.Tag;
import java.util.List;

public interface TagService {
    void saveTag(Tag tag);

    void saveBlogAndTag(Long blog_id, Long tag_id);

    void deleteTag(Long id);

    void deleteBlogAndTag(Long blog_id);

    void updateTag(Tag tag);

    void updateBlogAndTag(Long blog_id, Long tag_id);

    Tag getById(Long id);

    Tag getByName(String name);

    List<Tag> getSearch(String name);

    List<Tag> getAllTag();

    BaseRes getfirstPageTag();

    BaseRes  getAdminTag();

    List<Tag> getTagsByBlogId(Long tag_id);
}
