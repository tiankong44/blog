package com.tiankong44.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tiankong44.base.entity.BaseRes;
import com.tiankong44.dao.BlogMapper;
import com.tiankong44.dao.TagMapper;
import com.tiankong44.model.Blog;
import com.tiankong44.model.Tag;
import com.tiankong44.service.TagService;
import com.tiankong44.util.ConstantUtil;
import com.tiankong44.util.JsonUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @ClassName TagServiceImpl
 * @Description TODO
 * @Author 12481
 * @Date 19:40
 * @Version 1.0
 **/
@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private BlogMapper blogMapper;

    @Override
    public void saveTag(Tag tag) {


        tagMapper.saveTag(tag);

    }

    @Override
    public void saveBlogAndTag(Long blog_id, Long tag_id) {
        tagMapper.saveBlogAndTag(blog_id, tag_id);
    }

    @Override
    public void deleteTag(Long id) {
        tagMapper.deleteTag(id);
    }

    @Override
    public void deleteBlogAndTag(Long blog_id) {
        tagMapper.deleteBlogAndTag(blog_id);
    }

    @Override
    public void updateTag(Tag tag) {
        tagMapper.updateTag(tag);
    }

    @Override
    public void updateBlogAndTag(Long blog_id, Long tag_id) {
        tagMapper.updateBlogAndTag(blog_id, tag_id);
    }

    @Override
    public Tag getById(Long id) {
        return tagMapper.getById(id);
    }

    @Override
    public Tag getByName(String name) {
        return tagMapper.getByName(name);
    }

    @Override
    public List<Tag> getSearch(String name) {
        return tagMapper.getSearch(name);
    }

    @Override
    public BaseRes getBlogListByTagId(String msg) {

        BaseRes res = new BaseRes();
        JSONObject reqJson = null;
        int pageNum = 1;// 当前页 从1开始
        int pageSize = 8;// 一页显示多少条
        try {
            reqJson = JSONObject.fromObject(msg);
            // 检查字段是否为空
            Map<?, ?> checkMap = JsonUtils.noNulls(reqJson, "pageNum", "pageSize", "tagId");
            if (checkMap != null) {
                res.setCode(ConstantUtil.RESULT_FAILED);
                res.setDesc("请求参数错误");
                return res;
            }
            pageNum = reqJson.getInt("pageNum");
            pageSize = reqJson.getInt("pageSize");
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(ConstantUtil.RESULT_FAILED);
            res.setDesc("请求参数异常");
            return res;
        }
        String tagIdstr = reqJson.getString("tagId");
        Long tagId = Long.parseLong(tagIdstr);
        PageHelper.startPage(pageNum, pageSize);
        List<Blog> blogList = tagMapper.getByTagId(tagId);
        PageInfo<Blog> pageInfo = new PageInfo<>(blogList);
        for (Blog blog : blogList) {
            //Blog blog1 = blogMapper.getBlogById(blog.getId());
            List<Tag> tagList = tagMapper.getTagsByBlogId(blog.getId());
            String showTags = " ";
            for (Tag tag : tagList) {
                showTags += tag.getName() + "  ";
            }
            Long user_id = blogMapper.getUserIdByBlogId(blog.getId());
            blog.setUser_id(user_id);
            blog.setUser(blogMapper.getUser(user_id));
            blog.setTags(tagList);
            blog.setShowTags(showTags);


        }
        res.setCode(ConstantUtil.RESULT_SUCCESS);
        res.setDesc("博客列表查询成功");
        res.setData(pageInfo);
        return res;
    }

    @Override
    public List<Tag> getAllTag() {
        return tagMapper.getAllTag();
    }

    @Override
    public BaseRes getfirstPageTag() {
        BaseRes res = new BaseRes();
        List<Tag> tagList = tagMapper.getfirstPageTag();
        tagList.remove(0);
        res.setData(tagList);
        res.setDesc("标签列表查询成功");
        res.setCode(ConstantUtil.RESULT_SUCCESS);
        return res;
    }

    @Override
    public List<Tag> getAdminTag() {
        return tagMapper.getAdminTag();
    }

    @Override
    public List<Tag> getTagsByBlogId(Long blog_id) {
        return tagMapper.getTagsByBlogId(blog_id);
    }
}
