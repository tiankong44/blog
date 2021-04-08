package com.tiankong44.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author zhanghao_SMEICS
 * @Date 2020/11/23  15:21
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Student {
    private int id;
    private String name;
    private int age;
    private String updateTime;

}
