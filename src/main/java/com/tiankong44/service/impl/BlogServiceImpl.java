package com.tiankong44.service.impl;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.tiankong44.base.entity.BaseRes;
import com.tiankong44.dao.BlogMapper;
import com.tiankong44.dao.CommentMapper;
import com.tiankong44.dao.TagMapper;
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
    @Autowired
    private TagMapper tagMapper;

    public BlogServiceImpl() {
    }

    public BaseRes getBlogDetail(String msg, HttpServletRequest request) {
        BaseRes res = new BaseRes();
        User user = (User) request.getSession().getAttribute(User.SESSION_KEY);
        if (user == null) {
            res.setCode(ConstantUtil.RESULT_FAILED);
            res.setDesc("请先登录！");
            return res;
        }
        Long userId = user.getId();
        JSONObject reqJson = null;
        try {
            reqJson = JSONObject.fromObject(msg);
            Map<?, ?> checkMap = JsonUtils.noNulls(reqJson, "blogId");
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

        Blog blog = blogMapper.getBlogDetailById(Long.parseLong(reqJson.getString("blogId")));
        if (blog != null) {
            res.setData(blog);
            res.setDesc("博客详情查询成功");
            res.setCode(ConstantUtil.RESULT_SUCCESS);
        } else {
            res.setDesc("系统错误");
            res.setCode(ConstantUtil.RESULT_FAILED);
        }
        return res;
    }

    public Blog getBlogById(Long id) {
        BaseRes res = new BaseRes();

        Blog blog = this.blogMapper.getBlogById(id);
        List<Tag> tagList = this.tagService.getTagsByBlogId(blog.getId());
        blog.setTags(tagList);
        Long user_id = this.blogMapper.getUserIdByBlogId(blog.getId());
        blog.setUserId(user_id);
        blog.setUser(this.blogMapper.getUser(user_id));
        return blog;
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

        List<Blog> blogs = blogMapper.getAllBlog();
        List<Blog> tblogs = new ArrayList();
        Map<Long, Double> blogMap = new TreeMap();
        if (blogTitle == null) {
            res.setData(tblogs);
            res.setCode(0);
            res.setDesc("暂时没有推荐的博客");
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
            Iterator iterator = blogs.iterator();

            while (iterator.hasNext()) {
                Blog blog = (Blog) iterator.next();
                double d0 = CosineSimilarity.getSimilarity(blogTitlestr, blog.getTitle());
                List<Tag> tagList = blog.getTags();
                String showTags = " ";
                Tag tag;
                for (Iterator it = tagList.iterator(); it.hasNext(); showTags = showTags + tag.getName()) {
                    tag = (Tag) it.next();
                }

                blog.setShowTags(showTags.trim());
                double d1 = CosineSimilarity.getSimilarity(blogTagStr, blog.getShowTags());
                double d2 = CosineSimilarity.getSimilarity(blogDescStr, blog.getDescription());
                double d = d0 * 0.25D + d1 * 0.5D + d2 * 0.25D;

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
        }
        return res;
    }

    public BaseRes queryBlogList(String msg, HttpServletRequest request) {
        BaseRes res = new BaseRes();
        User user = (User) request.getSession().getAttribute(User.SESSION_KEY);
        if (user == null) {
            res.setCode(ConstantUtil.RESULT_FAILED);
            res.setDesc("请先登录！");
            return res;
        }
        Long id = user.getId();
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
        Map<String, Object> paramMap = new HashMap<>();


        if (reqJson.containsKey("tagIds")) {
            String tagIds = reqJson.getString("tagIds");
            String tags[] = tagIds.split(",");
            if (!StringUtil.isEmpty(tagIds)) {
                paramMap.put("list", tags);
            }
        }
        paramMap.put("userId", id);
        if (reqJson.containsKey("title")) {
            String title = reqJson.getString("title");
            if (!StringUtil.isEmpty(title)) {
                paramMap.put("title", title);
            }
        }
        if (reqJson.containsKey("recommend")) {
//            String recommend = reqJson.getString("recommend");
            boolean flag = reqJson.getBoolean("recommend");
            if (flag) {
                paramMap.put("recommend", 1);
            } else {
                paramMap.put("recommend", 0);
            }

        }
        PageHelper.startPage(reqJson.getInt("pageNum"), reqJson.getInt("pageSize"), true);
        List<Long> blogIdList = blogMapper.queryBlogIdList(paramMap);
        PageInfo<Long> pageInfo = null;
        List<Blog> blogList = new ArrayList<>();
        if (blogIdList.size() > 0) {
            blogList = blogMapper.queryBlogList(blogIdList);
            pageInfo = new PageInfo<Long>(blogIdList);
        } else {
            pageInfo = new PageInfo<Long>(new ArrayList<>());
        }
        PageInfo<Blog> blogPageInfo = new PageInfo<Blog>();
        blogPageInfo.setList(blogList);
        blogPageInfo.setHasPreviousPage(pageInfo.isHasPreviousPage());
        blogPageInfo.setHasNextPage(pageInfo.isHasNextPage());
        blogPageInfo.setEndRow(pageInfo.getEndRow());
        blogPageInfo.setNavigateFirstPage(pageInfo.getNavigateFirstPage());
        blogPageInfo.setTotal(pageInfo.getTotal());
        blogPageInfo.setPages(pageInfo.getPages());
        blogPageInfo.setPrePage(pageInfo.getPrePage());
        blogPageInfo.setNextPage(pageInfo.getNextPage());
        blogPageInfo.setPageNum(pageInfo.getPageNum());

        res.setCode(ConstantUtil.RESULT_SUCCESS);
        res.setDesc("博客列表查询成功");
        res.setData(blogPageInfo);
        return res;
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

    @Transactional(rollbackFor = Exception.class)
    public BaseRes saveBlog(String msg, HttpServletRequest request) {
        BaseRes res = new BaseRes();
        User user = (User) request.getSession().getAttribute(User.SESSION_KEY);
        if (user == null) {
            res.setCode(ConstantUtil.RESULT_FAILED);
            res.setDesc("请先登录！");
            return res;
        }
        JSONObject reqJson = null;
        try {
            reqJson = JSONObject.fromObject(msg);
            Map<?, ?> checkMap = JsonUtils.noNulls(reqJson, "title", "content", "description", "published", "commentabled", "appreciation", "tagIds");
            if (checkMap != null) {
                res.setCode(ConstantUtil.RESULT_FAILED);
                res.setDesc("请求参数错误");
                return res;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        String tagIds = reqJson.getString("tagIds");
        String[] tags = tagIds.split(",");
        StringBuilder stringBuilder = new StringBuilder();
        for (String tagStr : tags) {
            //检查是否新增标签
            Long tagId = null;
            Tag tag = null;
            try {
                tagId = Long.parseLong(tagStr);
            } catch (Exception e) {
                tagId = 0L;
            }
            tag = tagService.getById(tagId);

            if (tag == null) {
                //新增标签
                tag = new Tag();
                tag.setName(tagStr);
                Long saveTagId = tagService.saveTag(tag, user);
                stringBuilder.append(saveTagId.toString()).append(",");

                //新增标签和用户关系
            } else {
                stringBuilder.append(tagStr).append(",");
            }
        }
        Blog blog = new Blog();
        blog.setTitle(reqJson.getString("title"));
        blog.setContent(reqJson.getString("content"));

        if (reqJson.containsKey("firstPic") && !StringUtil.isEmpty(reqJson.getString("firstPic"))) {
            blog.setFirstPicture(reqJson.getString("firstPic"));
        } else {

            blog.setFirstPicture(ConstantUtil.DEFAULTFIRSTPIC);
        }
        blog.setDescription(reqJson.getString("description"));
        boolean published = reqJson.getBoolean("published");
        blog.setPublished(published);
        boolean commentabled = reqJson.getBoolean("commentabled");
        blog.setCommentabled(commentabled);

        boolean appreciation = reqJson.getBoolean("appreciation");
        blog.setAppreciation(appreciation);
        String newTagIds = stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1);
        blog.setTag_ids(newTagIds);
        String createTime = DateUtil.format(new Date(), "yyyy-MM-dd hh:mm:ss");
        blog.setCreateTime(createTime);
        blog.setUpdateTime(createTime);
        blog.setUserId(user.getId());
        boolean flag = blogMapper.saveBlog(blog);
        if (!flag) {
            res.setCode(ConstantUtil.RESULT_FAILED);
            res.setDesc("新增博客失败！");
            return res;
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("blogId", blog.getId());
        paramMap.put("list", newTagIds.split(","));
        int num = tagMapper.saveBlogAndTag(paramMap);
        if (num > 0) {
            res.setCode(ConstantUtil.RESULT_SUCCESS);
            res.setDesc("新增博客成功");
        } else {
            res.setCode(ConstantUtil.RESULT_FAILED);
            res.setDesc("新增博客失败！");
        }
        return res;
    }

    @Override
    public BaseRes updateBlog(String msg, HttpServletRequest request) {
        BaseRes res = new BaseRes();
        User user = (User) request.getSession().getAttribute(User.SESSION_KEY);
        if (user == null) {
            res.setCode(ConstantUtil.RESULT_FAILED);
            res.setDesc("请先登录！");
            return res;
        }
        JSONObject reqJson = null;
        try {
            reqJson = JSONObject.fromObject(msg);
            Map<?, ?> checkMap = JsonUtils.noNulls(reqJson, "blogId", "title", "firstPic", "content", "description", "published", "commentabled", "appreciation", "tagIds");
            if (checkMap != null) {
                res.setCode(ConstantUtil.RESULT_FAILED);
                res.setDesc("请求参数错误");
                return res;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String tagIds = reqJson.getString("tagIds");
        String[] tags = tagIds.split(",");
        StringBuilder stringBuilder = new StringBuilder();
        for (String tagStr : tags) {
            //检查是否新增标签
            Long tagId = null;
            Tag tag = null;
            try {
                tagId = Long.parseLong(tagStr);
            } catch (Exception e) {
                tagId = 0L;
            }
            tag = tagService.getById(tagId);

            if (tag == null) {
                //新增标签
                tag = new Tag();
                tag.setName(tagStr);
                Long saveTagId = tagService.saveTag(tag, user);
                stringBuilder.append(saveTagId.toString()).append(",");

                //新增标签和用户关系
            } else {
                stringBuilder.append(tagStr).append(",");
            }
        }

        Blog blog = new Blog();
        blog.setId(Long.parseLong(reqJson.getString("blogId")));
        blog.setTitle(reqJson.getString("title"));
        blog.setContent(reqJson.getString("content"));
        if (reqJson.containsKey("firstPic") && !StringUtil.isEmpty(reqJson.getString("firstPic"))) {
            blog.setFirstPicture(reqJson.getString("firstPic"));
        } else {

            blog.setFirstPicture(ConstantUtil.DEFAULTFIRSTPIC);
        }
        blog.setDescription(reqJson.getString("description"));
        boolean published = reqJson.getBoolean("published");
        blog.setPublished(published);
        boolean commentabled = reqJson.getBoolean("commentabled");
        blog.setCommentabled(commentabled);

        boolean appreciation = reqJson.getBoolean("appreciation");
        blog.setAppreciation(appreciation);
        String newTagIds = stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1);
        blog.setTag_ids(newTagIds);
        String createTime = DateUtil.format(new Date(), "yyyy-MM-dd hh:mm:ss");
//        blog.setCreateTime(createTime);
        blog.setUpdateTime(createTime);
        blog.setUserId(user.getId());
        boolean flag = blogMapper.updateBlog(blog);
        if (!flag) {
            res.setCode(ConstantUtil.RESULT_FAILED);
            res.setDesc("编辑博客失败！");
            return res;
        }
        Map<String, Object> paramMap = new HashMap<>();
        tagMapper.deleteBlogAndTag(blog.getId());

        paramMap.put("blogId", blog.getId());
        paramMap.put("list", newTagIds.split(","));
        int num = tagMapper.saveBlogAndTag(paramMap);
        if (num > 0) {
            res.setCode(ConstantUtil.RESULT_SUCCESS);
            res.setDesc("编辑博客成功");
        } else {
            res.setCode(ConstantUtil.RESULT_FAILED);
            res.setDesc("编辑博客失败！");
        }
        return res;
    }

    public BaseRes deleteBlog(String msg) {
        BaseRes res = new BaseRes();
        JSONObject reqJson = null;
        try {
            reqJson = JSONObject.fromObject(msg);
            Map<?, ?> checkMap = JsonUtils.noNulls(reqJson, "blogId");
            if (checkMap != null) {
                res.setCode(ConstantUtil.RESULT_FAILED);
                res.setDesc("请求参数错误");
                return res;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String blogId = reqJson.getString("blogId");
        boolean flag = false;
        flag = blogMapper.deleteBlog(Long.parseLong(blogId));
        if (!flag) {
            res.setCode(ConstantUtil.RESULT_FAILED);
            res.setDesc("博客删除失败");
            return res;
        }
        flag = tagMapper.deleteBlogAndTag(Long.parseLong(blogId));
        if (!flag) {
            res.setCode(ConstantUtil.RESULT_FAILED);
            res.setDesc("博客删除失败");
        } else {
            res.setCode(ConstantUtil.RESULT_SUCCESS);
            res.setDesc("博客删除成功");
        }
        return res;
    }

    public BaseRes getAllRecommendBlog() {
        BaseRes res = new BaseRes();
        return res;
    }

    public BaseRes getFiveNewCommentBlog() {
        BaseRes res = new BaseRes();
        Map<String, Object> blogMap = new TreeMap();
        List<Blog> blogList = this.commentMapper.getFiveNewComment();
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
        } else {
            res.setCode(1);
            res.setDesc("浏览量最高的五篇博客查询失败");
        }
        return res;
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
