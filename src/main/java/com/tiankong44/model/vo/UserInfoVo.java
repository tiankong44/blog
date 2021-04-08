package com.tiankong44.model.vo;

import com.tiankong44.model.Skill;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author zhanghao_SMEICS
 * @Date 2021/3/10  11:24
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoVo {
    private String userId;
    private String username;
    private int age;
    private String nickName;
    private String email;
    private String avatar;
    private String introduction;
    private int sex;
    private String birthDay;
    private String qq;
    private List<Skill> skills;
}
