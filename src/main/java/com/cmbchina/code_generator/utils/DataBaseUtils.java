package com.cmbchina.code_generator.utils;

import com.cmbchina.code_generator.entity.Table;

/**
 * 数据库工具类
 * @author Bin
 */

public class DataBaseUtils {
    /**
     * 根据全局配置的url截取数据库名称
     * @author Bin
     * @datatime
     * @param url
     * @return
     */

    public static String getDatabaseName(String url)
    {
        return url.substring(url.lastIndexOf('/') + 1, url.indexOf('?'));
    }
}
