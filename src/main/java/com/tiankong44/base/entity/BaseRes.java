package com.tiankong44.base.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 接口返回基础类
 *
 * @author miaoyi
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseRes {
    // 返回状态码
    private int code; //0 成功 1 失败  999 需要登录访问
    // 描述
    private String desc;
    // 返回对象
    private Object data;


}
