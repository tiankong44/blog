package com.tiankong44.controller.frontController;

import com.tiankong44.base.entity.BaseRes;
import com.tiankong44.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/archivie")
public class ArchivingController {
    @Autowired
    BlogService blogService;


    /**
     * 获取标签列表
     *
     * @param request
     * @param msg
     * @return
     * @author zhanghao_SMEICS
     * @Date 2020/10/24 17:59
     */

    @RequestMapping({"/getArchive"})
    @ResponseBody
    public BaseRes getArchive(@RequestBody String msg) {
        BaseRes res = new BaseRes();
        res = this.blogService.getArchivingBlog(msg);
        return res;

    }


}
