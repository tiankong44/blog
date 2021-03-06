package com.tiankong44.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tiankong44.base.entity.BaseRes;
import com.tiankong44.dao.BlogMapper;
import com.tiankong44.dao.CommentMapper;
import com.tiankong44.model.Blog;
import com.tiankong44.model.Comment;
import com.tiankong44.model.Tag;
import com.tiankong44.model.User;
import com.tiankong44.model.vo.ArchiveVo;
import com.tiankong44.service.BlogService;
import com.tiankong44.service.CommentService;
import com.tiankong44.util.*;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private TagServiceImpl tagService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private CommentMapper commentMapper;

    public BlogServiceImpl() {
    }

    public Blog getBlogById(Long id) {
        Blog blog = this.blogMapper.getBlogById(id);
        List<Tag> tagList = this.tagService.getTagsByBlogId(blog.getId());
        blog.setTags(tagList);
        Long user_id = this.blogMapper.getUserIdByBlogId(blog.getId());
        blog.setUserId(user_id);
        blog.setUser(this.blogMapper.getUser(user_id));
        return blog;
    }

    public List<Blog> getByCondition(String title, List<Long> ids, boolean recommend) {
        return this.blogMapper.getByCondition(title, ids, recommend);
    }

    public BaseRes getBlogAndConvert(HttpSession session, String msg, HttpServletRequest request) {
        BaseRes res = new BaseRes();
        new TreeMap();
        JSONObject reqJson = JSONObject.fromObject(msg);
        Map<?, ?> checkMap = JsonUtils.noNulls(reqJson, new String[]{"id"});
        if (checkMap != null) {
            res.setCode(1);
            res.setDesc("请求参数错误");
            return res;
        } else {
            Long id = reqJson.getLong("id");
            String ip = request.getRemoteAddr();
            if (!this.redisUtil.hasKey(ip + id + "view")) {
                this.blogMapper.updateBlogViews(id);
                this.redisUtil.setEx(ip + id + "view", ip + id, 3L, TimeUnit.DAYS);
            }

            Map blog = this.blogMapper.getBlogByBlogId(id);
            String content = (String) blog.get("content");
            blog.put("content", MarkdownUtil.markdownToHtml(content));
            List<Tag> tagList = this.tagService.getTagsByBlogId(id);
            blog.put("tags", tagList);
            if (this.redisUtil.hasKey(ip + "blogTitle")) {
                String blogTitle = this.redisUtil.get(ip + "blogTitle");
                this.redisUtil.append(ip + "blogTitle", (String) blog.get("title") + "-");
            } else {
                this.redisUtil.setEx(ip + "blogTitle", (String) blog.get("title"), 3L, TimeUnit.DAYS);
            }

            List<Tag> tags = (List) blog.get("tags");
            String blogDesc;
            if (this.redisUtil.hasKey(ip + "blogTag")) {
                blogDesc = this.redisUtil.get(ip + "blogTag");
                Iterator var15 = tags.iterator();

                while (var15.hasNext()) {
                    Tag tag = (Tag) var15.next();
                    this.redisUtil.append(ip + "blogTag", tag.getName() + ",");
                }
            } else {
                for (int i = 0; i < tags.size(); ++i) {
                    if (i == 0) {
                        this.redisUtil.setEx(ip + "blogTag", ((Tag) tags.get(0)).getName() + ",", 3L, TimeUnit.DAYS);
                    } else {
                        this.redisUtil.append(ip + "blogTag", ((Tag) tags.get(i)).getName() + ",");
                    }
                }
            }

            if (this.redisUtil.hasKey(ip + "blogDesc")) {
                blogDesc = this.redisUtil.get(ip + "blogDesc");
                this.redisUtil.append(ip + "blogDesc", blog.get("description") + "-");
            } else {
                this.redisUtil.setEx(ip + "blogDesc", blog.get("description") + "-", 3L, TimeUnit.DAYS);
            }

            res.setCode(0);
            res.setData(blog);
            return res;
        }
    }

    public BaseRes getrecommend(HttpServletRequest request) {
        BaseRes res = new BaseRes();
        Map<String, Object> resultMap = new TreeMap();
        String ip = request.getRemoteAddr();
        String blogTitle = "";
        String blogTag = "";
        String blogDesc = "";
        if (this.redisUtil.hasKey(ip + "blogTag")) {
            blogTag = this.redisUtil.get(ip + "blogTag");
        }

        if (this.redisUtil.hasKey(ip + "blogTitle")) {
            blogTitle = this.redisUtil.get(ip + "blogTitle");
        }

        if (this.redisUtil.hasKey(ip + "blogDesc")) {
            blogDesc = this.redisUtil.get(ip + "blogDesc");
        }

        List<Blog> blogs = this.getAllBlog();
        List<Blog> tblogs = new ArrayList();
        Map<Long, Double> blogMap = new TreeMap();
        if (blogTitle == null) {
            res.setData(tblogs);
            res.setCode(0);
            res.setDesc("暂时没有推荐的博客");
            return res;
        } else {
            int titleLen = blogTitle.length();
            String subTitlestr = "";
            if (titleLen > 50) {
                subTitlestr = blogTitle.substring(titleLen - 50, titleLen);
            } else {
                subTitlestr = blogTitle;
            }

            int descLen = blogDesc.length();
            String subDescstr = "";
            if (descLen > 200) {
                subDescstr = blogDesc.substring(descLen - 200, descLen);
            } else {
                subDescstr = blogDesc;
            }

            String blogTitlestr = subTitlestr.replace("null", "");
            blogTitlestr = blogTitlestr.replaceAll("-", "");
            String blogTagStr = blogTag.replace("null", "");
            String blogDescStr = subDescstr.replace("null", "");
            blogDescStr = blogDescStr.replaceAll("-", "");
            Iterator var18 = blogs.iterator();

            while (var18.hasNext()) {
                Blog blog = (Blog) var18.next();
                double d0 = CosineSimilarity.getSimilarity(blogTitlestr, blog.getTitle());
                List<Tag> tagList = this.tagService.getTagsByBlogId(blog.getId());
                String showTags = " ";

                Tag tag;
                for (Iterator it = tagList.iterator(); it.hasNext(); showTags = showTags + tag.getName()) {
                    tag = (Tag) it.next();
                }

                blog.setShowTags(showTags.trim());
                double d1 = CosineSimilarity.getSimilarity(blogTagStr, blog.getShowTags());
                double d2 = CosineSimilarity.getSimilarity(blogDescStr, blog.getDescription());
                double d = d0 * 0.25D + d1 * 0.5D + d2 * 0.25D;
                System.out.println("d:" + d + "================");
                if (d >= 0.25D) {
                    blogMap.put(blog.getId(), d);
                }
            }

            List<Entry<Long, Double>> list = new ArrayList(blogMap.entrySet());
            Collections.sort(list, new Comparator<Entry<Long, Double>>() {
                public int compare(Entry<Long, Double> o1, Entry<Long, Double> o2) {
                    return ((Double) o1.getValue()).compareTo((Double) o2.getValue());
                }
            });
            int size = list.size();
            Entry m;
            int i;
            if (size < 5) {
                for (i = size - 1; i >= 0; --i) {
                    m = (Entry) list.get(i);
                    tblogs.add(this.getBlogById((Long) m.getKey()));
                }
            } else {
                for (i = size - 1; i >= size - 5; --i) {
                    m = (Entry) list.get(i);
                    tblogs.add(this.getBlogById((Long) m.getKey()));
                }
            }

            resultMap.put("blogList", tblogs);
            res.setCode(0);
            res.setData(resultMap);
            res.setDesc("已向您推荐一些博客");
            return res;
        }
    }

    public List<Blog> getBlogByUserId(Long user_id) {
        return this.blogMapper.getBlogByUserId(user_id);
    }

    public List<Blog> getAllBlog() {
        return this.blogMapper.getAllBlog();
    }

    public Long getUserIdByBlogId(Long id) {
        return this.blogMapper.getUserIdByBlogId(id);
    }

    public User getUser(Long user_id) {
        return this.blogMapper.getUser(user_id);
    }

    public void saveBlog(Blog blog) {
        this.blogMapper.saveBlog(blog);
    }

    public Long getMaxBlogId() {
        return this.blogMapper.getMaxBlogId();
    }

    public void deleteBlog(Long id) {
        this.blogMapper.deleteBlog(id);
    }

    public void updateBlog(Blog blog) {
        this.blogMapper.updateBlog(blog);
    }

    public BaseRes getAllRecommendBlog() {
        BaseRes res = new BaseRes();
        return res;
    }

    public BaseRes getFiveNewCommentBlog() {
        BaseRes res = new BaseRes();
        Map<String, Object> blogMap = new TreeMap();
        List<Blog> blogList = this.commentMapper.getFiveNewComment();
//        List<Blog> blogList = new ArrayList();
//        Iterator it = commentList.iterator();
//
//        while (it.hasNext()) {
//            Comment comment = (Comment) it.next();
//            blogList.add(this.blogMapper.getBlogById(comment.getBlogId()));
//        }

        blogMap.put("blogList", blogList);
        res.setCode(0);
        res.setData(blogMap);
        res.setDesc("最新评论的的五篇博客查询成功");
        return res;
    }

    public BaseRes getTopFiveViewBlog() {
        BaseRes res = new BaseRes();
        Map<String, Object> blogMap = new TreeMap();
        List<Blog> list = this.blogMapper.getTopFiveViewBlog();
        if (list != null && list.size() > 0) {
            blogMap.put("blogList", list);
            res.setCode(0);
            res.setData(blogMap);
            res.setDesc("浏览量最高的五篇博客查询成功");
            return res;
        } else {
            res.setCode(1);
            res.setDesc("浏览量最高的五篇博客查询失败");
            return res;
        }
    }

    public BaseRes getFiveNewBlog() {
        BaseRes res = new BaseRes();
        Map<String, Object> blogMap = new TreeMap();
        List<Blog> list = this.blogMapper.getFiveNewBlog();
        if (list != null && list.size() > 0) {
            blogMap.put("blogList", list);
            res.setCode(0);
            res.setData(blogMap);
            res.setDesc("最新五篇博客查询成功");
            return res;
        } else {
            res.setCode(1);
            res.setDesc("浏览量最高的五篇博客查询失败");
            return res;
        }
    }

    public BaseRes getFirstPageBlog(String msg) {
        BaseRes res = new BaseRes();
        JSONObject reqJson = null;
        int pageNum;
        int pageSize;
        try {
            reqJson = JSONObject.fromObject(msg);
            Map<?, ?> checkMap = JsonUtils.noNulls(reqJson, new String[]{"pageNum", "pageSize"});
            if (checkMap != null) {
                res.setCode(1);
                res.setDesc("请求参数错误");
                return res;
            }

            pageNum = reqJson.getInt("pageNum");
            pageSize = reqJson.getInt("pageSize");
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(1);
            res.setDesc("请求参数异常");
            return res;
        }

        PageHelper.startPage(pageNum, pageSize);
        List<Map> blogList = this.blogMapper.getFirstPageBlog();
        PageInfo<Map> pageInfo = new PageInfo(blogList);
        res.setCode(0);
        res.setData(pageInfo);
        res.setDesc("查询博客列表成功");
        return res;
    }

    public BaseRes getFirstPageSearch(String msg) {
        BaseRes res = new BaseRes();
        JSONObject reqJson = null;
        int pageNum;
        int pageSize;
        try {
            reqJson = JSONObject.fromObject(msg);
            Map<?, ?> checkMap = JsonUtils.noNulls(reqJson, new String[]{"pageNum", "pageSize", "title"});
            if (checkMap != null) {
                res.setCode(1);
                res.setDesc("请求参数错误");
                return res;
            }

            pageNum = reqJson.getInt("pageNum");
            pageSize = reqJson.getInt("pageSize");
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(1);
            res.setDesc("请求参数异常");
            return res;
        }

        String title = reqJson.getString("title");
        PageHelper.startPage(pageNum, pageSize);
        List<Blog> blogList = this.blogMapper.getFirstPageSearch(title);
        PageInfo pageInfo = new PageInfo(blogList);
        res.setCode(0);
        res.setData(pageInfo);
        res.setDesc("查询博客列表成功");
        return res;
    }

    public BaseRes getArchivingBlog(String msg) {
        BaseRes res = new BaseRes();
        Map result = new HashMap();
        JSONObject reqJson = null;
        List<ArchiveVo> archiveVos = new ArrayList<>();
        int pageNum;
        int pageSize;
        try {
            reqJson = JSONObject.fromObject(msg);
            Map<?, ?> checkMap = JsonUtils.noNulls(reqJson, "pageNum", "pageSize");
            if (checkMap != null) {
                res.setCode(1);
                res.setDesc("请求参数错误");
                return res;
            }

            pageNum = reqJson.getInt("pageNum");
            pageSize = reqJson.getInt("pageSize");
            PageHelper.startPage(pageNum, pageSize);
            List<Blog> blogList = blogMapper.getArchivingBlog();
            PageInfo<Map> pageInfo = new PageInfo(blogList);

            result.put("pageInfo", pageInfo);
//        Map<String, List<Blog>> blogMap = new TreeMap<>();
            List<String> dateList = new ArrayList<>();


            for (int i = 0; i < blogList.size(); i++) {

                String date = blogList.get(i).getCreateTime();
                if (dateList.contains(date)) {

                    for (ArchiveVo archiveVo : archiveVos) {
                        if (archiveVo.getDate().equals(date)) {
                            archiveVo.getList().add(blogList.get(i));
                        }
                    }

                } else {
                    dateList.add(date);
                    ArchiveVo archiveVo = new ArchiveVo();
                    archiveVo.setDate(date);
                    List dateList1 = new ArrayList();
                    dateList1.add(blogList.get(i));
                    archiveVo.setList(dateList1);
                    archiveVos.add(archiveVo);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(1);
            res.setDesc("请求参数异常");
            return res;
        }
        result.put("data", archiveVos);
        res.setCode(ConstantUtil.RESULT_SUCCESS);
        res.setData(result);
        res.setDesc("归档列表获取成功");
        return res;
    }

    public void updateBlogViews(Long id) {
        this.blogMapper.updateBlogViews(id);
    }

    @Override
    @Transactional
    public BaseRes updateBlogPraise(HttpServletRequest request, String msg) {
        BaseRes res = new BaseRes();
        JSONObject reqJson = null;

        reqJson = JSONObject.fromObject(msg);
        Map<?, ?> checkMap = JsonUtils.noNulls(reqJson, "id");
        if (checkMap != null) {
            res.setCode(1);
            res.setDesc("请求参数错误");
            return res;
        }
        Long id = reqJson.getLong("id");
        String ip = request.getRemoteAddr() + id + "praise";
        if (redisUtil.hasKey(ip)) {
            res.setCode(ConstantUtil.RESULT_FAILED);
            res.setDesc("你已经点过赞了！");
        } else {
            blogMapper.updateBlogPraise(id);
            redisUtil.setEx(ip, ip, 3L, TimeUnit.DAYS);
            String praise = blogMapper.getPraiseByBlogId(id);
            res.setCode(ConstantUtil.RESULT_SUCCESS);
            res.setData(praise);
            res.setDesc("点赞成功！");
            return res;
        }


        return res;
    }

    @Override
    public BaseRes getComment(String msg) {
        BaseRes res = new BaseRes();
        JSONObject reqJson = null;
        try {
            reqJson = JSONObject.fromObject(msg);
            Map<?, ?> checkMap = JsonUtils.noNulls(reqJson, "id");
            if (checkMap != null) {
                res.setCode(1);
                res.setDesc("请求参数错误");
                return res;
            }
            Long id = reqJson.getLong("id");
            List<Comment> comments = commentMapper.getCommentsByBlogId(id);
            res.setCode(ConstantUtil.RESULT_SUCCESS);
            res.setData(comments);
            res.setDesc("评论列表获取成功");
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(1);
            res.setDesc("请求参数异常");
            return res;
        }
        return res;
    }

    public List<Long> getBlogIdByTagId(Long tag_id) {
        return this.blogMapper.getBlogIdByTagId(tag_id);
    }

    public BaseRes getBlogListByTagId(String msg) {
        BaseRes res = new BaseRes();
        JSONObject reqJson = null;
        int pageNum;
        int pageSize;
        try {
            reqJson = JSONObject.fromObject(msg);
            Map<?, ?> checkMap = JsonUtils.noNulls(reqJson, new String[]{"pageNum", "pageSize", "tagId"});
            if (checkMap != null) {
                res.setCode(1);
                res.setDesc("请求参数错误");
                return res;
            }

            pageNum = reqJson.getInt("pageNum");
            pageSize = reqJson.getInt("pageSize");
        } catch (Exception e) {
            e.printStackTrace();
            res.setCode(1);
            res.setDesc("请求参数异常");
            return res;
        }

        String tagIdstr = reqJson.getString("tagId");
        Long tagId = Long.parseLong(tagIdstr);
        PageHelper.startPage(pageNum, pageSize);
        List<Blog> blogList = this.blogMapper.getBlogListByTagId(tagId);
        PageInfo<Blog> pageInfo = new PageInfo(blogList);
        res.setCode(0);
        res.setDesc("博客列表查询成功");
        res.setData(pageInfo);
        return res;
    }
}
