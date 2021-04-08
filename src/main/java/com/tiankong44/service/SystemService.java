package com.tiankong44.service;

import com.tiankong44.base.entity.BaseRes;

import javax.servlet.http.HttpServletRequest;

public interface SystemService {
    /**
     * 详情页展示
     *
     * @return
     * @author zhanghao_SMEICS
     * @Date 2021/3/11 11:42
     */

    BaseRes getUserInfo(HttpServletRequest request);

    /**
     * 登录信息
     *
     * @return
     * @author zhanghao_SMEICS
     * @Date 2021/3/11 11:42
     */
    BaseRes getSysUserInfo(HttpServletRequest request);

    boolean updateAvatar(String path, String userId);
}
