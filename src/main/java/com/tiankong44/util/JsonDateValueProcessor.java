package com.tiankong44.util;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 描述：网上抄的 http://blog.csdn.net/lgqiang2010/article/details/48542673
 * Created on 2017/10/16.
 * <p>Title:</p>
 * <p>Copyright:Copyright (c) 2017</p>
 * <p>Company:北京思特奇信息技术股份有限公司</p>
 * <p>Department:NBC</p>
 */
public class JsonDateValueProcessor implements JsonValueProcessor {
    private String format ="yyyy-MM-dd";

    public JsonDateValueProcessor() {
        super();
    }

    public JsonDateValueProcessor(String format) {
        super();
        this.format = format;
    }

    @Override
    public Object processArrayValue(Object paramObject,
                                    JsonConfig paramJsonConfig) {
        return process(paramObject);
    }

    @Override
    public Object processObjectValue(String paramString, Object paramObject,
                                     JsonConfig paramJsonConfig) {
        return process(paramObject);
    }


    private Object process(Object value){
        if(value instanceof Date){
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
            return sdf.format(value);
        }
        return value == null ? "" : value.toString();
    }

}