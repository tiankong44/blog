package com.tiankong44.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**

 * @Author zhanghao_SMEICS
 * @Date 2021/4/11  16:28
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PhotoVo {

    private Long id;
    private String imgName;
    private String path;
    private Long userId;
    private String uploadDate;
    private int type;  //1.相册图片。2.图床图片
    private Long albumId;
    private boolean ischeck;

}
