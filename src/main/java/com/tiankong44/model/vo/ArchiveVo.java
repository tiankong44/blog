package com.tiankong44.model.vo;

import com.tiankong44.model.Blog;
import lombok.Data;

import java.util.List;

/**
 * @param request
 * @param msg
 * @Author zhanghao_SMEICS
 * @Date 2020/10/24  20:55
 **/
@Data
public class ArchiveVo {
    String Date;
    private List<Blog> list;
}
