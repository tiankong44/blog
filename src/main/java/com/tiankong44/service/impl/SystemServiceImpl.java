package com.tiankong44.service.impl;

import com.tiankong44.base.entity.BaseRes;
import com.tiankong44.dao.SystemMapper;
import com.tiankong44.model.User;
import com.tiankong44.model.vo.UserInfoVo;
import com.tiankong44.model.vo.UserVo;
import com.tiankong44.service.SystemService;
import com.tiankong44.util.ConstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author zhanghao_SMEICS
 * @Date 2021/3/10  11:28
 **/
@Service
public class SystemServiceImpl implements SystemService {
    @Autowired
    SystemMapper systemMapper;

    @Override
    public BaseRes getUserInfo(HttpServletRequest request) {
        BaseRes res = new BaseRes();
        User user = (User) request.getSession().getAttribute(User.SESSION_KEY);
        if (user != null) {
            Long userId = user.getId();
            UserInfoVo userInfoVo = systemMapper.getUserInfo(userId);
            if (userInfoVo != null) {
                res.setCode(ConstantUtil.RESULT_SUCCESS);
                res.setData(userInfoVo);
                res.setDesc("获取用户信息成功");
            }
        } else {
            res.setCode(ConstantUtil.RESULT_FAILED);
            res.setDesc("用户未登录，无法获取用户信息");
        }
        return res;
    }

    @Override
    public BaseRes getSysUserInfo(HttpServletRequest request) {
        BaseRes res = new BaseRes();
        User user = (User) request.getSession().getAttribute(User.SESSION_KEY);
        UserVo userVo = new UserVo();
        if (user != null) {
            Long userId = user.getId();
            userVo = systemMapper.getSysUserInfo(userId);
            if (userVo != null) {
                userVo.setLogged(true);
                res.setCode(ConstantUtil.RESULT_SUCCESS);
                res.setData(userVo);
                res.setDesc("获取用户信息成功");
            }
        } else {
            userVo.setLogged(false);
            res.setData(userVo);
            res.setCode(ConstantUtil.RESULT_FAILED);
            res.setDesc("用户未登录，无法获取用户信息");
        }
        return res;
    }

    @Override
    public boolean updateAvatar(String path, String userId) {
        //
        boolean logFlag = systemMapper.insertAvatarLog(userId);
        boolean flag = systemMapper.updateAvatar(path, userId);
        return flag;
    }
}
