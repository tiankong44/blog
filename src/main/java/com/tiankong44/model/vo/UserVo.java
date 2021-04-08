package com.tiankong44.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户信息_前端显示
 *
 * @author miaoyi
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userId;
    private String username;
    private String avatar;//头像
    private String email;//
    private String nickname;//
    private boolean logged; // 是否登录 "false":未登录，"true"：已登录

}
