package com.tiankong44.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author zhanghao_SMEICS
 * @Date 2021/3/10  15:53
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Skill {
    private int skillId;
    private Long userId;
    private int percentage;
    private String skillName;
}
