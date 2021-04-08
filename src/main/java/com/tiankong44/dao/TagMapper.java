package com.tiankong44.dao;

import com.tiankong44.model.Blog;
import com.tiankong44.model.Tag;
import com.tiankong44.model.vo.TagVo;
import com.tiankong44.model.vo.UserTag;
import net.sf.json.JSONObject;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface TagMapper {
    boolean saveTag(Tag tag);

    int saveBlogAndTag(Map<String, Object> paramMap);

    boolean deleteTag(JSONObject reqJson);

    boolean deleteBlogAndTag(Long blog_id);

    boolean deleteBlogAndTags(List<Long> list);
    void updateTag(Tag tag);



    Tag getById(Long id);

    List<Tag> getfirstPageTag();


    Tag getByName(String name);

    List<Tag> getTagsByBlogId(Long blog_id);

    List<Tag> getAdminTag();

    List<Blog> getByTagId(Long tagId);

    List<TagVo> getTagsByUserId(Long userId);

    boolean saveTagAndUser(Long tagId, Long userId);


    List<TagVo> queryTagList(JSONObject reqJson);

    boolean deleteTagAndUser(JSONObject reqJson);

    UserTag getTagByUserIdAndTagId(JSONObject reqJson);
}
