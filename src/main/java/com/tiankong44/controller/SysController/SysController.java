package com.tiankong44.controller.SysController;

import com.tiankong44.base.entity.BaseRes;
import com.tiankong44.model.User;
import com.tiankong44.service.SystemService;
import com.tiankong44.util.ConstantUtil;
import com.tiankong44.util.QiniuUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @Author zhanghao_SMEICS
 * @Date 2021/3/10  11:22
 **/
@RestController
@RequestMapping("/system")
public class SysController {
    @Autowired
    SystemService systemService;

    @RequestMapping("/getUserInfo")
    public BaseRes getUserInfo(HttpServletRequest request) {
        BaseRes res = new BaseRes();
        res = systemService.getUserInfo(request);
        return res;
    }

    @RequestMapping("/getSysUserInfo")
    public BaseRes getSysUserInfo(HttpServletRequest request) {
        BaseRes res = new BaseRes();
        res = systemService.getSysUserInfo(request);
        return res;
    }

    @PostMapping("/updateAvatar")
    @ResponseBody
    public BaseRes uploadAvatar(@Param("file") MultipartFile file,
                                HttpServletRequest request) throws Exception {
        BaseRes res = new BaseRes();
        String path = "";
        User user = (User) request.getSession().getAttribute(User.SESSION_KEY);
        Long userId = user.getId();
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        path = QiniuUpload.updateFile(file, uuid);
        boolean flag = systemService.updateAvatar(path, Long.toString(userId));
        if (flag) {
            res.setData(path);
            res.setCode(ConstantUtil.RESULT_SUCCESS);
            res.setDesc("头像上传成功");
        } else {
            res.setCode(ConstantUtil.RESULT_FAILED);
            res.setDesc("头像上传失败");
        }
        return res;
    }
}
