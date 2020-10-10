package com.tiankong44.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tiankong44.base.entity.BaseRes;
import com.tiankong44.dao.BlogMapper;
import com.tiankong44.model.Blog;
import com.tiankong44.model.Comment;
import com.tiankong44.model.Tag;
import com.tiankong44.model.User;
import com.tiankong44.service.BlogService;
import com.tiankong44.service.CommentService;
import com.tiankong44.util.ConstantUtil;
import com.tiankong44.util.JsonUtils;
import com.tiankong44.util.MarkdownUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName BlogServiceImpl
 * @Description TODO
 * @Author 12481
 * @Date 20:59
 * @Version 1.0
 **/
@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private TagServiceImpl tagService;
    @Autowired
    private CommentService commentService;

    @Override
    public Blog getBlogById(Long id) {
        Blog blog = blogMapper.getBlogById(id);
        List<Tag> tagList = tagService.getTagsByBlogId(blog.getId());
        blog.setTags(tagList);
        Long user_id = blogMapper.getUserIdByBlogId(blog.getId());
        blog.setUser_id(user_id);
        blog.setUser(blogMapper.getUser(user_id));
        return blog;
    }

    @Override
    public List<Blog> getByCondition(String title, List<Long> ids, boolean recommend) {
        return blogMapper.getByCondition(title, ids, recommend);
    }

    @Override
    public Blog getBlogAndConvert(Long id) {
        Blog blog = blogMapper.getBlogById(id);
        String content = blog.getContent();
        // Md2x md2x=new Md2x();
        blog.setContent(MarkdownUtil.markdownToHtml(content));
        // blog.setContent(MDTool.markdown2Html(content));
        //  blog.setContent(  md2x.parse(content));
        List<Tag> tagList = tagService.getTagsByBlogId(blog.getId());
        blog.setTags(tagList);
        Long user_id = blogMapper.getUserIdByBlogId(blog.getId());
        blog.setUser_id(user_id);
        blog.setUser(blogMapper.getUser(user_id));
        return blog;
    }

    @Override
    public List<Blog> getBlogByUserId(Long user_id) {
        return blogMapper.getBlogByUserId(user_id);
    }

    @Override
    public List<Blog> getAllBlog() {
        return blogMapper.getAllBlog();
    }


    @Override
    public Long getUserIdByBlogId(Long id) {
        return blogMapper.getUserIdByBlogId(id);
    }

    @Override
    public User getUser(Long user_id) {
        return blogMapper.getUser(user_id);
    }

    @Override
    public void saveBlog(Blog blog) {
        blogMapper.saveBlog(blog);
    }

    @Override
    public Long getMaxBlogId() {
        return blogMapper.getMaxBlogId();
    }

    @Override
    public void deleteBlog(Long id) {
        blogMapper.deleteBlog(id);
    }

    @Override
    public void updateBlog(Blog blog) {
        blogMapper.updateBlog(blog);
    }

    @Override
    public BaseRes getAllRecommendBlog() {
        BaseRes res = new BaseRes();
//        Map<String, Object> blogMap = new HashMap<String, Object>();
//        List<Blog> list = blogMapper.getAllRecommendBlog()
//        if (list != null && list.size() > 0) {
//            blogMap.put("blogList", list);
//            res.setCode(ConstantUtil.RESULT_SUCCESS);
//            res.setData(blogMap);
//            res.setDesc("浏览量最高的五篇博客查询成功");
//            return res;
//        } else {
//            res.setCode(ConstantUtil.RESULT_FAILED);
////            res.setData(blogMap);
//            res.setDesc("浏览量最高的五篇博客查询失败");
//            return res;
//        }
        return res;
    }

    @Override
    public BaseRes getFiveNewCommentBlog() {
        BaseRes res = new BaseRes();
        Map<String, Object> blogMap = new HashMap<String, Object>();
        List<Comment> commentList = commentService.getFiveNewComment();
        List<Blog> blogList = new ArrayList<>();
        for (Comment comment : commentList) {
            blogList.add(blogMapper.getBlogById(comment.getBlogId()));
        }
        blogMap.put("blogList", blogList);
        res.setCode(ConstantUtil.RESULT_SUCCESS);
        res.setData(blogMap);
        res.setDesc("最新评论的的五篇博客查询成功");
        return res;
    }

    @Override
    public BaseRes getTopFiveViewBlog() {
        BaseRes res = new BaseRes();
        Map<String, Object> blogMap = new HashMap<String, Object>();
        List<Blog> list = blogMapper.getTopFiveViewBlog();
        if (list != null && list.size() > 0) {
            blogMap.put("blogList", list);
            res.setCode(ConstantUtil.RESULT_SUCCESS);
            res.setData(blogMap);
            res.setDesc("浏览量最高的五篇博客查询成功");
            return res;
        } else {
            res.setCode(ConstantUtil.RESULT_FAILED);
//            res.setData(blogMap);
            res.setDesc("浏览量最高的五篇博客查询失败");
            return res;
        }
    }

    @Override
    public BaseRes getFiveNewBlog() {
        BaseRes res = new BaseRes();
        Map<String, Object> blogMap = new HashMap<String, Object>();
        List<Blog> list = blogMapper.getFiveNewBlog();
        if (list != null && list.size() > 0) {
            blogMap.put("blogList", list);
            res.setCode(ConstantUtil.RESULT_SUCCESS);
            res.setData(blogMap);
            res.setDesc("最新五篇博客查询成功");
            return res;
        } else {
            res.setCode(ConstantUtil.RESULT_FAILED);
//            res.setData(blogMap);
            res.setDesc("浏览量最高的五篇博客查询失败");
            return res;
        }
    }


    @Override
    public BaseRes getFirstPageBlog(String msg) {
        BaseRes res = new BaseRes();
        JSONObject reqJson = null;
        int pageNum = 1;// 当前页 从1开始
        int pageSize = 8;// 一页显示多少条
        try {
            reqJson = JSONObject.fromObject(msg);
            // 检查字段是否为空
            Map<?, ?> checkMap = JsonUtils.noNulls(reqJson, "pageNum", "pageSize");
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
        PageHelper.startPage(pageNum, pageSize);
        List<Blog> blogList = blogMapper.getFirstPageBlog();

        for (Blog blog : blogList) {
            List<Tag> tagList = tagService.getTagsByBlogId(blog.getId());
            String showTags = " ";
            for (Tag tag : tagList) {
                showTags += tag.getName() + "  ";
            }
            Long user_id = blogMapper.getUserIdByBlogId(blog.getId());
            blog.setUser_id(user_id);
            blog.setUser(blogMapper.getUser(user_id));
            blog.setTags(tagList);
            blog.setShowTags(showTags);
            String desc = blog.getDescription();
            if (desc.length() > 200) {
                desc = desc.substring(0, 200) + "...";
                blog.setDescription(desc);
            }
        }
        PageInfo<Blog> pageInfo = new PageInfo<>(blogList);
        res.setCode(ConstantUtil.RESULT_SUCCESS);
        res.setData(pageInfo);
        res.setDesc("查询博客列表成功");
        return res;
    }

    @Override
    public BaseRes getFirstPageSearch(String msg) {
        BaseRes res = new BaseRes();
        JSONObject reqJson = null;
        int pageNum = 1;// 当前页 从1开始
        int pageSize = 8;// 一页显示多少条
        try {
            reqJson = JSONObject.fromObject(msg);
            // 检查字段是否为空
            Map<?, ?> checkMap = JsonUtils.noNulls(reqJson, "pageNum", "pageSize", "title");
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
        String title = reqJson.getString("title");
        PageHelper.startPage(pageNum, pageSize);
        List<Blog> blogList = blogMapper.getFirstPageSearch(title);

        for (Blog blog : blogList) {
            List<Tag> tagList = tagService.getTagsByBlogId(blog.getId());
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
        PageInfo pageInfo = new PageInfo(blogList);
        res.setCode(ConstantUtil.RESULT_SUCCESS);
        res.setData(pageInfo);
        res.setDesc("查询博客列表成功");
        return res;
    }

    @Override
    public List<Blog> getArchivingBlog() {
        return blogMapper.getArchivingBlog();
    }

    @Override
    public void updateBlogViews(Long id) {
        blogMapper.updateBlogViews(id);
    }

    @Override
    public void updateBlogPraise(Long id) {
        blogMapper.updateBlogPraise(id);
    }


    @Override
    public List<Long> getBlogIdByTagId(Long tag_id) {
        return blogMapper.getBlogIdByTagId(tag_id);
    }
}
