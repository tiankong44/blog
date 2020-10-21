//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.tiankong44.service.impl;

import com.tiankong44.base.entity.BaseRes;
import com.tiankong44.dao.BlogMapper;
import com.tiankong44.dao.TagMapper;
import com.tiankong44.model.Tag;
import com.tiankong44.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private BlogMapper blogMapper;

    public TagServiceImpl() {
    }

    public void saveTag(Tag tag) {
        this.tagMapper.saveTag(tag);
    }

    public void saveBlogAndTag(Long blog_id, Long tag_id) {
        this.tagMapper.saveBlogAndTag(blog_id, tag_id);
    }

    public void deleteTag(Long id) {
        this.tagMapper.deleteTag(id);
    }

    public void deleteBlogAndTag(Long blog_id) {
        this.tagMapper.deleteBlogAndTag(blog_id);
    }

    public void updateTag(Tag tag) {
        this.tagMapper.updateTag(tag);
    }

    public void updateBlogAndTag(Long blog_id, Long tag_id) {
        this.tagMapper.updateBlogAndTag(blog_id, tag_id);
    }

    public Tag getById(Long id) {
        return this.tagMapper.getById(id);
    }

    public Tag getByName(String name) {
        return this.tagMapper.getByName(name);
    }

    public List<Tag> getSearch(String name) {
        return this.tagMapper.getSearch(name);
    }

    public List<Tag> getAllTag() {
        return this.tagMapper.getAllTag();
    }

    public BaseRes getfirstPageTag() {
        BaseRes res = new BaseRes();
        List<Tag> tagList = this.tagMapper.getfirstPageTag();
        tagList.remove(0);
        res.setData(tagList);
        res.setDesc("标签列表查询成功");
        res.setCode(0);
        return res;
    }

    public List<Tag> getAdminTag() {
        return this.tagMapper.getAdminTag();
    }

    public List<Tag> getTagsByBlogId(Long blog_id) {
        return this.tagMapper.getTagsByBlogId(blog_id);
    }
}
