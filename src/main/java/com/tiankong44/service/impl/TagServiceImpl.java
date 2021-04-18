//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.tiankong44.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tiankong44.base.entity.BaseRes;
import com.tiankong44.dao.BlogMapper;
import com.tiankong44.dao.TagMapper;
import com.tiankong44.model.Tag;
import com.tiankong44.model.User;
import com.tiankong44.model.vo.TagVo;
import com.tiankong44.model.vo.UserTag;
import com.tiankong44.service.TagService;
import com.tiankong44.util.ConstantUtil;
import com.tiankong44.util.JsonUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private BlogMapper blogMapper;

    public TagServiceImpl() {
    }

    @Override
    @Transactional(timeout = 1000, rollbackFor = Exception.class)
    public BaseRes addTag(String msg, User user) {
        BaseRes res = new BaseRes();
        JSONObject reqJson = null;
        try {
            reqJson = JSONObject.fromObject(msg);
            Map<?, ?> checkMap = JsonUtils.noNulls(reqJson, "tagName");
            if (checkMap != null) {
                res.setCode(ConstantUtil.RESULT_FAILED);
                res.setDesc("请求参数错误");
                return res;
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(ConstantUtil.RESULT_FAILED);
            res.setDesc("请求参数错误");
            return res;
        }

        String tagName = reqJson.getString("tagName");

        Tag oldTag = tagMapper.getByName(tagName);
        boolean flag = false;
        Tag tag = new Tag();
        tag.setName(tagName);
        if (oldTag != null) {
            reqJson.put("tagId", oldTag.getId());
            reqJson.put("userId", user.getId());
            UserTag userTag = tagMapper.getTagByUserIdAndTagId(reqJson);
            if (userTag != null) {
                res.setCode(ConstantUtil.RESULT_FAILED);
                res.setDesc("标签已存在！");
                return res;
            }
            flag = tagMapper.saveTagAndUser(oldTag.getId(), user.getId());
            if (!flag) {
                res.setCode(ConstantUtil.RESULT_FAILED);
                res.setDesc("新增标签失败！");
                return res;
            }
        } else {
            flag = tagMapper.saveTag(tag);
            if (flag) {
                flag = tagMapper.saveTagAndUser(tag.getId(), user.getId());
            } else {
                res.setCode(ConstantUtil.RESULT_FAILED);
                res.setDesc("新增标签失败！");
                return res;
            }
        }
        res.setCode(ConstantUtil.RESULT_SUCCESS);
        res.setDesc("新增标签成功！");
        return res;
    }

    @Transactional(timeout = 1000, rollbackFor = Exception.class)
    public Long saveTag(Tag tag, User user) {
        String tagName = tag.getName();
        Tag oldTag = tagMapper.getByName(tagName);
        boolean flag = false;
        if (oldTag != null) {
            flag = tagMapper.saveTagAndUser(oldTag.getId(), user.getId());
            return oldTag.getId();
        } else {
            flag = tagMapper.saveTag(tag);
            if (flag) {
                flag = tagMapper.saveTagAndUser(tag.getId(), user.getId());
            }
            return tag.getId();
        }

    }

    @Override
    public BaseRes queryTagList(String msg, User user) {
        BaseRes res = new BaseRes();
        JSONObject reqJson = null;
        try {
            reqJson = JSONObject.fromObject(msg);
            Map<?, ?> checkMap = JsonUtils.noNulls(reqJson, "pageNum", "pageSize");
            if (checkMap != null) {
                res.setCode(ConstantUtil.RESULT_FAILED);
                res.setDesc("请求参数错误");
                return res;
            }

        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(ConstantUtil.RESULT_FAILED);
            res.setDesc("请求参数错误");
            return res;
        }
        reqJson.put("userId", user.getId());
        PageHelper.startPage(reqJson.getInt("pageNum"), reqJson.getInt("pageSize"), true);
        List<TagVo> tagList = tagMapper.queryTagList(reqJson);
        PageInfo<TagVo> pageInfo = new PageInfo<>(tagList);
        res.setCode(ConstantUtil.RESULT_SUCCESS);
        res.setData(pageInfo);
        res.setDesc("标签列表查询成功！");
        return res;
    }

    //    public void saveBlogAndTag(Long blog_id, Long tag_id) {
//        this.tagMapper.saveBlogAndTag(blog_id, tag_id);
//    }
    @Override
    @Transactional(timeout = 1000, rollbackFor = Exception.class)
    public BaseRes deleteTag(String msg, User user) {
        BaseRes res = new BaseRes();
        JSONObject reqJson = null;
        try {
            reqJson = JSONObject.fromObject(msg);
            Map<?, ?> checkMap = JsonUtils.noNulls(reqJson, "tagId");
            if (checkMap != null) {
                res.setCode(ConstantUtil.RESULT_FAILED);
                res.setDesc("请求参数错误");
                return res;
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(ConstantUtil.RESULT_FAILED);
            res.setDesc("请求参数错误");
            return res;
        }
        //检查  当前用户的  当前标签是否关联博客
        reqJson.put("userId", user.getId());
        List<Long> blogList = blogMapper.getBlogIdByTagIdAndUserId(reqJson);
        boolean flag = false;
        if (blogList.size() > 0) {
            flag = tagMapper.deleteBlogAndTags(blogList);
            if (!flag) {
                res.setCode(ConstantUtil.RESULT_FAILED);
                res.setDesc("标签删除失败！");
                return res;
            }
        }
        //删除 用户 标签 关联
        flag = tagMapper.deleteTagAndUser(reqJson);

        if (!flag) {
            res.setCode(ConstantUtil.RESULT_FAILED);
            res.setDesc("标签删除失败！");
        } else {
            res.setCode(ConstantUtil.RESULT_SUCCESS);
            res.setDesc("标签删除成功！");
        }
        return res;
    }
    @Transactional(timeout = 1000, rollbackFor = Exception.class)
    public void deleteBlogAndTag(Long blogId) {
        this.tagMapper.deleteBlogAndTag(blogId);
    }
    @Transactional(timeout = 1000, rollbackFor = Exception.class)
    public BaseRes updateTag(String msg, User user) {
        BaseRes res = new BaseRes();
        JSONObject reqJson = null;
        try {
            reqJson = JSONObject.fromObject(msg);
            Map<?, ?> checkMap = JsonUtils.noNulls(reqJson, "tagId", "newTagName");
            if (checkMap != null) {
                res.setCode(ConstantUtil.RESULT_FAILED);
                res.setDesc("请求参数错误");
                return res;
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(ConstantUtil.RESULT_FAILED);
            res.setDesc("请求参数错误");
            return res;
        }
        String newTagName = reqJson.getString("newTagName");
        reqJson.put("userId", user.getId());
        Tag oldTag = tagMapper.getByName(newTagName);


        boolean flag = false;
        if (oldTag != null) {

            //判断是否已经添加
            JSONObject param = new JSONObject();
            param.put("tagId", oldTag.getId());
            param.put("userId", user.getId());
            UserTag userTag = tagMapper.getTagByUserIdAndTagId(param);
            if (userTag!=null){
                res.setCode(ConstantUtil.RESULT_FAILED);
                res.setDesc("修改标签失败，标签已存在！");
                return res;
            }

            //非空 新的标签 也在tag表存在
            //删掉原来的user_tag表中的 旧标签

            flag = tagMapper.deleteTagAndUser(reqJson);
            if (!flag) {
                res.setCode(ConstantUtil.RESULT_FAILED);
                res.setDesc("修改标签失败！");
                return res;
            }
            //将 查询出的标签添加到user_tag表中
            flag = tagMapper.saveTagAndUser(oldTag.getId(), user.getId());
        } else {
            //为空    新标签  存入 tag表
            Tag newTag = new Tag();
            newTag.setName(newTagName);
            flag = tagMapper.saveTag(newTag);
            if (!flag) {
                res.setCode(ConstantUtil.RESULT_FAILED);
                res.setDesc("修改标签失败！");
                return res;
            }
            //删除原来的关系
            flag = tagMapper.deleteTagAndUser(reqJson);
            //存入user_tag表
            flag = tagMapper.saveTagAndUser(newTag.getId(), user.getId());

        }
        if (!flag) {
            res.setCode(ConstantUtil.RESULT_FAILED);
            res.setDesc("修改标签失败！");
            return res;
        }
        res.setCode(ConstantUtil.RESULT_SUCCESS);
        res.setDesc("修改标签成功！");
        return res;
    }



    public Tag getById(Long id) {
        return this.tagMapper.getById(id);
    }

    public Tag getByName(String name) {
        return this.tagMapper.getByName(name);
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

    /**
     * 获取所有标签
     *
     * @return
     * @author zhanghao_SMEICS
     * @Date 2020/10/24 18:28
     */

    public BaseRes getAdminTag() {
        BaseRes res = new BaseRes();
        List<Tag> tagList = tagMapper.getAdminTag();
        res.setCode(ConstantUtil.RESULT_SUCCESS);
        res.setData(tagList);
        res.setDesc("标签列表获取成功");
        return res;
    }

    public List<Tag> getTagsByBlogId(Long blog_id) {
        return this.tagMapper.getTagsByBlogId(blog_id);
    }

    @Override
    public BaseRes getTagsByUserId(Long userId) {
        BaseRes res = new BaseRes();
        List<TagVo> tagList = tagMapper.getTagsByUserId(userId);
        Map<String, String> resMap = new HashMap<>();

        res.setData(tagList);
        res.setCode(ConstantUtil.RESULT_SUCCESS);
        res.setDesc("标签列表查询成功");
        return res;
    }
}
