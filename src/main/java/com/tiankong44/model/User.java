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
    public static final String SESSION_KEY = User.class.getName();
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String avatar;
    private Date createTime;
    private Date updateTime;
    private List<Blog> blogs = new ArrayList();
    private List<Album> album = new ArrayList();
    private List<Gallery> Gallery = new ArrayList();

}
