package com.tiankong44.service;

import com.tiankong44.base.entity.BaseRes;
import com.tiankong44.model.Tag;
import com.tiankong44.model.User;

import java.util.List;

public interface TagService {
    Long saveTag(Tag tag, User user);

    BaseRes deleteTag(String msg, User user);

    void deleteBlogAndTag(Long blog_id);

    BaseRes updateTag(String msg,User user);


    Tag getById(Long id);

    Tag getByName(String name);


    BaseRes getfirstPageTag();

    BaseRes getAdminTag();

    List<Tag> getTagsByBlogId(Long tag_id);

    BaseRes getTagsByUserId(Long userId);

    BaseRes queryTagList(String msg,User user);

    BaseRes addTag(String msg, User user);
}
