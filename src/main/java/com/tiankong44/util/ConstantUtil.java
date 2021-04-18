package com.tiankong44.util;

import com.tiankong44.model.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author zhanghaoSMEICS
 * @Date 2020/10/1  13:33
 **/
@Component
public class ConstantUtil {
    private ConstantUtil() {
    }

    /**
     * log4j配置文件的路径
     */

    public static String DEFAULTFIRSTPIC;
    public static Tag[] DEFAULTTAGS;

    @Value("${defaultFirstPic}")
    public void setDEFAULTFIRSTPIC(String DEFAULTFIRSTPIC) {
        ConstantUtil.DEFAULTFIRSTPIC = DEFAULTFIRSTPIC;
    }

//    @Value("${defaultTags}")
//    public  void setDEFAULTTAGS(Tag[] DEFAULTTAGS) {
//        ConstantUtil.DEFAULTTAGS = DEFAULTTAGS;
//    }

    /**
     * 返回前端接口常量定义
     **/
    public static final int RESULT_SUCCESS = 0;//接口返回成功
    public static final int RESULT_FAILED = 1;//接口返回失败
    public static final int RESULT_NOAUTHORITY = 403;//没有权限访问
    public static final int RESULT_NOLOGIN = 500;//接口需要登录访问
    public static final String MAINPICTURE = "1";  //是否是主图
    public static final int ALBUM_PICTURE = 1;//相册图片
    public static final int GALLERY_PICTURE = 2;//图床图片

}
