package com.tiankong44.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Blog  {
    private Long id;
    //标题
    private String title;
    //内容
    private String content;
    //首图地址
    private String firstPicture;
    //浏览次数
    private Integer views;
    //点赞次数
    private int praise;
    //赞赏是否开启
    private boolean appreciation;
    //评论是否开始
    private boolean commentabled;

    //发布还是草稿
    private boolean published;
    //置顶
    private boolean top;
    //推荐
    private boolean recommend;
    //创建时间
    private String  createTime;
    //更新时间
    private String updateTime;

    //获取用户id
    private Long userId;
    //获取多个标签的id
    private String tag_ids;
    //描述
    private String description;

    //作者
    private User user;
    //标签列表字符串显示
    private  String showTags;
    //标签列表
    private List<Tag> tags = new ArrayList<>();
    //评论列表
    private List<Comment> comments = new ArrayList<>();
}
