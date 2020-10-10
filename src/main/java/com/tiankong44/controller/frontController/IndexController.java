package com.tiankong44.controller.frontController;

import com.tiankong44.base.entity.BaseRes;
import com.tiankong44.model.Blog;
import com.tiankong44.model.Tag;
import com.tiankong44.service.impl.BlogServiceImpl;
import com.tiankong44.service.impl.TagServiceImpl;
import com.tiankong44.util.CosineSimilarity;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping()
public class IndexController {
    @Autowired
    private BlogServiceImpl blogService;
    @Autowired
    private TagServiceImpl tagService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

//    @GetMapping("")
//    public String toIndex(Model model,
//                          @RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum, HttpServletRequest request) {
//        String ip = request.getRemoteAddr();
//        logger.info(ip + "来了！");
//
//        PageHelper.startPage(pageNum, 8);
//        List<Blog> blogList = blogService.getFirstPageBlog();
//        PageInfo<Blog> pageInfo = new PageInfo<>(blogList);
//        List<Tag> tagList = tagService.getfirstPageTag();
//        // List<Blog> topfiveBlog = blogService.getTopFiveViewBlog();
//        model.addAttribute("tagList", tagList);
//        //  model.addAttribute("topfiveBlog", topfiveBlog);
//        model.addAttribute("pageInfo", pageInfo);
//        return "index";
//    }

//    @RequestMapping("/index")
//    public String index(Model model,
//                        @RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum) {
//        PageHelper.startPage(pageNum, 8);
//        List<Blog> blogList = blogService.getFirstPageBlog();
//        PageInfo<Blog> pageInfo = new PageInfo<>(blogList);
//        List<Tag> tagList = tagService.getfirstPageTag();
//        // List<Blog> topfiveBlog = blogService.getTopFiveViewBlog();
//        model.addAttribute("tagList", tagList);
//        //  model.addAttribute("topfiveBlog", topfiveBlog);
//        model.addAttribute("pageInfo", pageInfo);
//        return "index";
//    }

//    @RequestMapping("/search")
//    @ResponseBody
//    public BaseRes search(@RequestBody String msg) {
//        BaseRes res = new BaseRes();
//       List<Blog> blogList = blogService.getFirstPageSearch(title);
//
//        return res;
//    }

    @RequestMapping("/getTopFiveBlog")
    @ResponseBody
    public BaseRes getTopFiveBlog() {
        BaseRes res = new BaseRes();
        res = blogService.getTopFiveViewBlog();
        return res;
    }

    /**
     * 已改造（最新五篇博客）
     *
     * @return
     */
    @RequestMapping("/getNewFiveBlog")
    @ResponseBody
    public BaseRes getNewFiveBlog() {
        BaseRes res = new BaseRes();
        res = blogService.getFiveNewBlog();
        return res;
    }

    /**
     * 已改造（最新五篇被评论的博客）
     *
     * @return
     */
    @RequestMapping("/getNewFiveCommentBlog")
    @ResponseBody
    public BaseRes getFiveNewCommentBlog() {
        BaseRes res = new BaseRes();
        res = blogService.getFiveNewCommentBlog();
        return res;
    }

    /**
     * 已改造（浏览量最高的五篇博客）
     *
     * @return
     */
    @RequestMapping("/getTopFiveViewBlog")
    @ResponseBody
    public BaseRes getTopFiveViewBlog() {
        BaseRes res = new BaseRes();
        res = blogService.getTopFiveViewBlog();
        return res;
    }


    @RequestMapping("/blogList")
    @ResponseBody
    public BaseRes blogPage(@RequestBody String msg) {
        BaseRes res = new BaseRes();
        JSONObject reqJson = JSONObject.fromObject(msg);

        String title = reqJson.getString("title");
        if (!"".equals(title) && title != null) {
            res = blogService.getFirstPageSearch(msg);
        } else {
            String tagId = reqJson.getString("tagId");
            if ("0".equals(tagId)) {
                res = blogService.getFirstPageBlog(msg);
            } else {
                res = tagService.getBlogListByTagId(msg);
            }
        }
        return res;
    }

    @RequestMapping("/tagList")
    @ResponseBody
    public BaseRes getTagList() {
        BaseRes res = new BaseRes();
        res = tagService.getfirstPageTag();
        return res;
    }

    @RequestMapping("/recommend")
    @ResponseBody
    public List<Blog> getrecommend(HttpSession session) {
        String blogTitle = (String) session.getAttribute("blogTitle");

        String blogTag = (String) session.getAttribute("blogTag");

        String blogDesc = (String) session.getAttribute("blogDesc");
        Long id = (Long) session.getAttribute("id");
        List<Blog> blogs = blogService.getAllBlog();
        List<Blog> tblogs = new ArrayList<>();
        Map<Long, Double> blogMap = new TreeMap<>();
        if (blogTitle == null) {
            return tblogs;
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
            //  blogTagStr = blogTagStr.replaceAll("-", "");

            String blogDescStr = subDescstr.replace("null", "");
            blogDescStr = blogDescStr.replaceAll("-", "");

            for (Blog blog : blogs) {
                double d0 = CosineSimilarity.getSimilarity(blogTitlestr, blog.getTitle());
                List<Tag> tagList = tagService.getTagsByBlogId(blog.getId());
                String showTags = " ";
                for (Tag tag : tagList) {
                    showTags += tag.getName();
                }
                blog.setShowTags(showTags.trim());
                double d1 = CosineSimilarity.getSimilarity(blogTagStr, blog.getShowTags());
                double d2 = CosineSimilarity.getSimilarity(blogDescStr, blog.getDescription());
                double d = (d0 * 0.25 + d1 * 0.5 + d2 * 0.25);
//                System.out.println(blog.getTitle());
//                System.out.println(d);
//                System.out.println(d0);
//                System.out.println(d1);
//                System.out.println(d2);
                if (d >= 0.25 && blog.getId() != id) {
                    blogMap.put(blog.getId(), d);
                }
            }
            List<Map.Entry<Long, Double>> list = new ArrayList<Map.Entry<Long, Double>>(blogMap.entrySet());

            Collections.sort(list, new Comparator<Map.Entry<Long, Double>>() {
                @Override
                public int compare(Map.Entry<Long, Double> o1, Map.Entry<Long, Double> o2) {
                    return o1.getValue().compareTo(o2.getValue());
                }
            });

            int size = list.size();
            if (size < 5) {
                for (int i = size - 1; i >= 0; i--) {
                    Map.Entry<Long, Double> m = list.get(i);
                    tblogs.add(blogService.getBlogById(m.getKey()));
                }
            } else {

                for (int i = size - 1; i >= size - 5; i--) {
                    Map.Entry<Long, Double> m = list.get(i);

                    tblogs.add(blogService.getBlogById(m.getKey()));

                }
            }
            return tblogs;
        }


    }
}
