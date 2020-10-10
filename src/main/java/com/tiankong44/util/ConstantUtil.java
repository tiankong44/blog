package com.tiankong44.util;/**
 * @Author zhanghao_SMEICS
 * @Date 2020/10/1$  13:33$
 * @param request
 * @param response
 **/

/**
 * @param request
 * @param response
 * @Author zhanghaoSMEICS
 * @Date 2020/10/1  13:33
 **/
public class ConstantUtil {
    private ConstantUtil() {
    }

    /**
     * 返回前端接口常量定义
     **/
    public static final int RESULT_SUCCESS = 0;//接口返回成功
    public static final int RESULT_FAILED = 1;//接口返回失败
    public static final int RESULT_NOAUTHORITY = 403;//没有权限访问
    public static final int RESULT_NOLOGIN = 500;//接口需要登录访问
}
