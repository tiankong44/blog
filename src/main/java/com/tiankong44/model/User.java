package com.tiankong44.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    private Long id;

    //用户名
    private String username;
    //密码
    private String password;
    //昵称
    private String nickname;
    //email
    private String email;
    //头像地址
    private String avatar;

    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //博客列表
    private List<Blog> blogs = new ArrayList<>();
    //图床
    private List<Album> album = new ArrayList<>();
    //相册
    private List<Gallery> Gallery = new ArrayList<>();
}
