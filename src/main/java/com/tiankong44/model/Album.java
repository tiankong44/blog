package com.tiankong44.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 相册
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Album {
    private Long id;
    //相册名
    private String albumName;

    //1.系统默认展示，2自定义
    private int type;
    //用户Id
    private Long userId;
    private String remark;//备注
    //更新时间
    private String createTime;

    private String mainPicture; //主图
    private List<Gallery> photos;
}
