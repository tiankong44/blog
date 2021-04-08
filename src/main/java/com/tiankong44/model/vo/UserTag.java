package com.tiankong44.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author zhanghao_SMEICS
 * @Date 2021/4/8  14:23
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTag {
    private Long userId;
    private Long tagId;
}
