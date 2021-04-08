package com.tiankong44.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 相册
 */
@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Album {
    private Long id;
    //图片名
    private String imgName;

    //路径
    private String path;
    //用户Id
    private Long userId;

    //更新时间
    private String uploadDate;

    List<Album> albumList = new ArrayList<>();
}
