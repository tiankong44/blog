package com.tiankong44.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Gallery {
    private Long id;
    private String imgName;
    private String uuid;
    private String path;
    private Long userId;
    private String uploadDate;
    private int type;  //1.相册图片。2.图床图片
    private Long albumId;

}
