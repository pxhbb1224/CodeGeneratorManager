package com.cmbchina.code_generator.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期时间工具类
 * @author Bin
 */

public class DateTimeUtils {

    /**
     * 获取当前时间字符串
     * 格式：yyyy-MM-dd hh:mm:ss
     * @author JohnDeng
     * @dateTime 2019年5月30日下午5:12:09
     * @return
     */
    public static String getDateTime()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return sdf.format(new Date());
    }

}
