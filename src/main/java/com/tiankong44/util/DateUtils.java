/*****************************************************************************
 * Copyright (c) 2015, www.qingshixun.com
 *
 * All rights reserved
 *
 *****************************************************************************/
package com.tiankong44.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间处理util 类
 *
 * @author QingShiXun
 * @version 1.0
 */
public class DateUtils {
    // 时间精确到秒的格式
    public final static String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public final static String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

    /**
     * 将时间以字符串的形式显示
     */
    public static final String timeToString(Date date) {
        return dateToString(date, DEFAULT_DATETIME_PATTERN);
    }

    public static final Date StringToDate(String str) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_PATTERN);
        Date date = sdf.parse(str);
        return date;
    }

    /**
     * 以"yyyy-MM-dd HH:mm:ss"形式展现时间
     */
    public static final String dateToString(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

    /**
     * 返回时间类型
     */
    public static final String[] SplitDate(String DateString) {
        //  DateString = "2020-02-01 - 2020-02-02";
        String[] str = DateString.split("-");
        String oldDate = "";
        String newDate = "";
        for (int i = 0; i < 6; i++) {
            if (i < 3) {
                if (i < 2) {
                    oldDate = oldDate + str[i].trim() + "-";
                } else {
                    oldDate += str[i].trim();
                }

            } else {
                if (i >= 3 && i < 5) {
                    newDate = newDate + str[i].trim() + "-";
                } else {
                    newDate += str[i].trim();
                }

            }
        }
        str[0] = oldDate.trim();
        str[1] = newDate.trim();
        String[] newstr = {str[0], str[1]};
        return newstr;
    }

    public static void main(String[] args) throws ParseException {
        // String[] str = SplitDate("2020-02-01 - 2020-02-02");
//        for (String s : str) {
//            System.out.println(s.trim());
//            System.out.println("--------------------------");
//            System.out.println(StringToDate(s).toString());
//
//        }
    }
}
