package com.tiankong44.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Message {
    private Long id;
    //昵称
    private String nickname;
    //邮箱
    private String email;
    //评论内容
    private String content;

    private boolean admin;
    //头像
    private String avatar;
    //创建时间
    private Date createTime;
    //父评论Id
    private Long parentCommentId;
    //父评论昵称
    private String parentNickname;
    //是否审核通过
    private boolean published;
    private Blog blog;
    //回复评论列表
    private List<Message> replyComments = new ArrayList<>();
    //父评论
    private Message parentComment;


}
